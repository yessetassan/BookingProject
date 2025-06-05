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
import com.yesset.booking.repositiry.UserRepository;
import com.yesset.booking.service.EmailService;
import com.yesset.booking.util.UserApp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.yesset.booking.entity.User;

import java.security.SecureRandom;
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
    private final EmailService javaMailSender;
    private final UserRepository userRepository;

    public AuthenticationService(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RoleRepository roleRepository, CategoryAdminRepository categoryAdminRepository, EmailService javaMailSender, UserRepository userRepository) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.categoryAdminRepository = categoryAdminRepository;
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
    }


    public ResponseAnswer<Object> signUp(SignUpRequest request) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(Boolean.TRUE);

        try {
            String password = generateRandomCode(8);
            Role role = roleRepository.findByName(ROLE_USER.toString()).orElse(null);
            var user = new User();
            user.setUsername(request.getEmail());
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setFirstname(request.getFirstName());
            user.setLastname(request.getLastName());
            user.setPhone(request.getPhone());
            user.setRole(role);
            userService.create(user);

            String subject = "Nova Rent — Account Registration | Тіркелу | Регистрация";

            String body = String.format("""
        Құрметті %s,

        Сіз Nova Rent жүйесіне сәтті тіркелдіңіз!

        Сіздің құпиясөзіңіз: %s

        --------------------------------------

        Уважаемый(ая) %s,

        Вы успешно зарегистрировались в системе Nova Rent!

        Ваш пароль: %s

        --------------------------------------

        Dear %s,

        You have successfully registered in the Nova Rent system!

        Your password: %s

        Best regards,  
        Nova Rent Team
        """, user.getFirstname(), password, user.getFirstname(), password, user.getFirstname(), password);
            javaMailSender.sendEmail(
                    user.getUsername(),
                    subject,
                    body
            );
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setStatus(USERNAME_EXISTS.toString());
            response.setReason(e.getMessage());
        }
        return response;
    }

    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
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
            response.setReason(e.getMessage());
        }
        return response;
    }

    public ResponseAnswer<Object> forgotPassword(String email) {
        ResponseAnswer<Object> response = new ResponseAnswer<>();
        response.setSuccess(true);
        try {
            User user = userRepository.findByUsername(email)
                    .orElseThrow();
            String password = generateRandomCode(8);
            user.setPasswordHash(passwordEncoder.encode(password));
            userRepository.save(user);

            String subject = "Nova Rent — Құпиясөзді қалпына келтіру | Восстановление пароля | Password Reset";

            String body = String.format("""
        Құрметті %s,

        Сіз Nova Rent жүйесінде құпиясөзіңізді қалпына келтіруді сұрадыңыз.

        Жаңа құпиясөзіңіз: %s

        --------------------------------------

        Уважаемый(ая) %s,

        Вы запросили восстановление пароля в системе Nova Rent.

        Ваш новый пароль: %s

        --------------------------------------

        Dear %s,

        You requested a password reset for your Nova Rent account.

        Your new password: %s

        Best regards,  
        Nova Rent Team
        """, user.getFirstname(), password, user.getFirstname(), password, user.getFirstname(), password);
            javaMailSender.sendEmail(
                    user.getUsername(),
                    subject,
                    body
            );
        }catch (Exception e){
            response.setSuccess(Boolean.FALSE);
            response.setReason(e.getMessage());
        }
        return response;
    }
}