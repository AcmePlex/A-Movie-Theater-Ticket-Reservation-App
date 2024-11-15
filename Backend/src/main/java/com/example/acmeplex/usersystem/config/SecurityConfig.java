package com.example.acmeplex.usersystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.inMemoryAuthentication()
    //         .withUser("admin")
    //         .password(bCryptPasswordEncoder.encode("adminpass"))
    //         .roles("ADMIN");
    // }

    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http
    //         .csrf().disable()
    //         .authorizeRequests()
    //             .antMatchers("/api/admin/**").hasRole("ADMIN")
    //             .antMatchers("/api/users/**", "/api/registered-users/**").permitAll()
    //             .anyRequest().authenticated()
    //         .and()
    //         .httpBasic();
    // }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
