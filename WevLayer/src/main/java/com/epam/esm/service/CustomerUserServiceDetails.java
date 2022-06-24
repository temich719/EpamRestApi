package com.epam.esm.service;

import com.epam.esm.dto.RoleUser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class CustomerUserServiceDetails implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public CustomerUserServiceDetails(UserService userService) {
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RoleUser roleUser = userService.getUserWithRole(username);
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(roleUser.getRole()));
        return new org.springframework.security.core.userdetails.User(roleUser.getUserName(), roleUser.getPassword(), authorities);
    }

}
