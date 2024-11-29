package com.example.acmeplex.usersystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
                // Authentication endpoints
                .requestMatchers("/api/auth/**").permitAll()
                
                // User Registration - Allow public access
                .requestMatchers(HttpMethod.POST, "/api/users/**").permitAll()
                
                // User-related GET endpoints - Require authentication
                .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated()
                
                // User Deletion - Restrict to ADMIN role
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                
                // Registered User Registration - Allow public access if applicable
                .requestMatchers(HttpMethod.POST, "/api/registered-users/**").permitAll()
                
                // Registered User-related GET endpoints - Require authentication
                .requestMatchers(HttpMethod.GET, "/api/registered-users/**").authenticated()
                
                // All other registration-related endpoints (e.g., PUT for updating) - need to allow public access
                .requestMatchers(HttpMethod.PUT, "/api/registered-users/**").permitAll()
                
                // Movie System Public GET Endpoints
                .requestMatchers(HttpMethod.GET, "/genres/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/movies/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/movie/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/theatres/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/showtimes/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/seats/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/movies/autocompletion/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/totalprice/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/cancel/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/ticketpayment/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/book/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/addcard").permitAll()
                
                // Movie System Protected Endpoints
                .requestMatchers(HttpMethod.PUT, "/membershippayment/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/movie-news").authenticated()
                
                // Payment Endpoints - Require authentication
                
                // Any other request requires authentication
                .anyRequest().authenticated();

        // Add JWT token filter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}