package com.yesset.booking.service.impl;

import com.yesset.booking.dto.response.ResponseAnswer;
import com.yesset.booking.dto.user.JwtAuthenticationResponse;
import com.yesset.booking.dto.user.SignInRequest;
import com.yesset.booking.dto.user.SignUpRequest;
import com.yesset.booking.entity.Category;
import com.yesset.booking.entity.CategoryAdmin;
import com.yesset.booking.entity.Role;
import com.yesset.booking.repositiry.CategoryAdminRepository;
import com.yesset.booking.repositiry.RoleRepository;
import com.yesset.booking.util.UserApp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.yesset.booking.entity.User;

import java.util.Objects;

import static com.yesset.booking.enums.constains.Role.ROLE_ADMIN;
import static com.yesset.booking.enums.constains.Role.ROLE_USER;
import static com.yesset.booking.util.AuthStatus.USERNAME_EXISTS;
import static com.yesset.booking.util.AuthStatus.USERNAME_OR_PASSWORD_MISMATCH;

@Service
@Slf4j
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final CategoryAdminRepository categoryAdminRepository;

    public AuthenticationService(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RoleRepository roleRepository, CategoryAdminRepository categoryAdminRepository) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.categoryAdminRepository = categoryAdminRepository;
    }

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public ResponseAnswer<JwtAuthenticationResponse> signUp(SignUpRequest request) {
        ResponseAnswer<JwtAuthenticationResponse> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);

        try {
            Role role = roleRepository.findByName(ROLE_USER.toString()).orElse(null);
            var user = new User();
            user.setUsername(request.getEmail());
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            user.setFirstname(request.getFirstName());
            user.setLastname(request.getLastName());
            user.setRole(role);

            userService.create(user);

            var jwt = jwtService.generateToken(user);
            response.setBody(new JwtAuthenticationResponse(jwt,user.getId(), user.getRole().getName(),""));
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setStatus(USERNAME_EXISTS.toString());
        }
        return response;
    }


    public ResponseAnswer<JwtAuthenticationResponse> signIn(SignInRequest request) {
        ResponseAnswer<JwtAuthenticationResponse> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));

            var user = userService
                    .userDetailsService()
                    .loadUserByUsername(request.getEmail());
            var jwt = jwtService.generateToken(user);
            User current = (User)user;
            Role role = current.getRole();
            if (role == null || role.getName() == null) throw new RuntimeException("Role not found");
            switch (role.getName()){
                case "ROLE_ADMIN": {
                    CategoryAdmin categoryAdmin = categoryAdminRepository.findByUser(current)
                            .orElseThrow(() -> new RuntimeException("CategoryAdmin not found"));
                    Category category = categoryAdmin.getCategory();
                    if (category == null || category.getName() == null)
                        throw new RuntimeException("CategoryAdmin not found");
                    response.setBody(new JwtAuthenticationResponse(jwt, current.getId(), current.getRole().getName(),category.getName()));
                } break;
                case "ROLE_USER": {
                    response.setBody(new JwtAuthenticationResponse(jwt, current.getId(), current.getRole().getName(),""));
                } break;
                default: throw new RuntimeException("Role not found");
            }
        }catch (Exception e){
            log.error("Ошибка: {}", e.getMessage());
            response.setSuccess(Boolean.FALSE);
            response.setStatus(USERNAME_OR_PASSWORD_MISMATCH.toString());
        }
        return response;
    }
}