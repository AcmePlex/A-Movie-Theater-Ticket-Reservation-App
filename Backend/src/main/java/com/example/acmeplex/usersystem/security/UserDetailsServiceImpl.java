package com.example.acmeplex.usersystem.security;

import com.example.acmeplex.usersystem.dto.RegisteredUserDTO;
import com.example.acmeplex.usersystem.model.RegisteredUser;
import com.example.acmeplex.usersystem.repository.RegisteredUserRepository;
import com.example.acmeplex.usersystem.service.RegisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Optional;

@Configuration
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RegisteredUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<RegisteredUser> registeredUser = repository.findByEmail(username);
        return registeredUser.map(UserInfoDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    public RegisteredUser addUser(RegisteredUser registeredUser) {
        registeredUser.setPassword(encoder.encode(registeredUser.getPassword()));
        return repository.save(registeredUser);
    }
}