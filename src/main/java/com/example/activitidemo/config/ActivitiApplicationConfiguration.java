package com.example.activitidemo.config;/*
 * @program: activiti-demo
 *
 * @description:
 *
 * @author: guangpeng.li
 *
 * @create: 2019-12-26 17:03
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class ActivitiApplicationConfiguration {
    @Bean
    public UserDetailsService myUserDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

        String[][] usersGroupAndRoles = {
                {"system", "password", "ROLE_ACTIVITI_USER"},
                {"admin", "password", "ROLE_ACTIVITI_ADMIN"}
        };

        for (String[] usersGroupAndRole : usersGroupAndRoles) {
            List<String> authoritiesStrings = Arrays.asList(Arrays.copyOfRange(usersGroupAndRole, 2, usersGroupAndRole.length));
            log.info("> Registering new user: {} with the following Authorities [{}]", usersGroupAndRole[0], authoritiesStrings);
            inMemoryUserDetailsManager.createUser(new User(usersGroupAndRole[0], passwordEncoder().encode(usersGroupAndRole[1]),
                    authoritiesStrings.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
        }

        return inMemoryUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
