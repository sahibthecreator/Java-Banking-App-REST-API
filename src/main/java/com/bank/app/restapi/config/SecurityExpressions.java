package com.bank.app.restapi.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component("securityExpressions")
public class SecurityExpressions {
    public boolean isSameUserOrEmployee(String userId, Authentication authentication) {
        UserData userData = (UserData) authentication.getPrincipal();
        String principalUserId = userData.getId().toString();
        return userId.equals(principalUserId)
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
    }

    public boolean hasEmployeeRole(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
    }
}
