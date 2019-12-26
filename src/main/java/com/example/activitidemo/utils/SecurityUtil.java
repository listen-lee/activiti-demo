package com.example.activitidemo.utils;/*
 * @program: activiti-demo
 *
 * @description:
 *
 * @author: guangpeng.li
 *
 * @create: 2019-12-26 16:48
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class SecurityUtil {

    @Qualifier("myUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    public void logInAs(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (null == userDetails) {
            throw new IllegalStateException(String.format("User %s doesn't exist, please provide a valid user", username));
        }
        log.info("> Logged in as: {}", username);
        SecurityContextHolder.setContext(new SecurityContextImpl(new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return userDetails.getAuthorities();
            }

            @Override
            public Object getCredentials() {
                return userDetails.getPassword();
            }

            @Override
            public Object getDetails() {
                return userDetails;
            }

            @Override
            public Object getPrincipal() {
                return userDetails;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }

            @Override
            public String getName() {
                return userDetails.getUsername();
            }
        }));

        org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(username);
    }
}
