package com.epam.esm.security.config;

import com.epam.esm.security.filter.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    private JwtFilter filterBean;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)

                .and()

                .authorizeRequests()

                .antMatchers(GET, "/certificates/*", "/certificates").permitAll()
                .antMatchers(POST, "/certificates", "/certificates/*").hasRole(ADMIN_ROLE)
                .antMatchers(DELETE, "/certificates", "/certificates/*").hasRole(ADMIN_ROLE)
                .antMatchers(PUT, "/certificates", "/certificates/*").hasRole(ADMIN_ROLE)

                .antMatchers(GET, "/orders/*").hasAnyRole(USER_ROLE, ADMIN_ROLE)

                .antMatchers(POST, "/tags/*").hasRole(ADMIN_ROLE)
                .antMatchers(POST, "/tags").hasRole(ADMIN_ROLE)
                .antMatchers(DELETE, "/tags/*").hasRole(ADMIN_ROLE)
                .antMatchers(GET, "/tags/*").permitAll()
                .antMatchers(GET, "/tags").permitAll()

                .antMatchers(POST, "/auth/*").permitAll()

                .antMatchers(POST, "/users").hasAnyRole(USER_ROLE, ADMIN_ROLE)
                .antMatchers(GET, "/users/orders/*").hasAnyRole(USER_ROLE, ADMIN_ROLE)
                .antMatchers(GET, "/users").hasAnyRole(USER_ROLE, ADMIN_ROLE)
                .antMatchers(GET, "/users/*").hasAnyRole(USER_ROLE, ADMIN_ROLE)

                .anyRequest().hasRole(ADMIN_ROLE)
                .and()
                .addFilterBefore(filterBean, UsernamePasswordAuthenticationFilter.class);
    }

}
