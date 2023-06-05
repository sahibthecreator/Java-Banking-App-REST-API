package com.bank.app.restapi.config;

import java.nio.file.AccessDeniedException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component("securityExpressions")
public class SecurityExpressions {
    public boolean isSameUserOrEmployee(String userId, Authentication authentication) throws AccessDeniedException {
        

        if (userId == null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE")))
            return true;

        UserData userData = (UserData) authentication.getPrincipal();
        String principalUserId = userData.getId().toString();
        return userId.equals(principalUserId)
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
    }

    public boolean hasEmployeeRole(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
    }

    public boolean loggedIn(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }

}
