package com.yesset.booking.util;

import com.yesset.booking.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserApp {
    public static   User getCurrentUser() {
        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                Object principal = auth.getPrincipal();
                if (principal instanceof User) {
                    return ((User) principal);
                }
            }
            return null;
        }catch (Exception e) {
            return null;
        }
    }
}
