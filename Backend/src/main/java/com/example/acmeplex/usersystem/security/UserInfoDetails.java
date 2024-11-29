package com.example.acmeplex.usersystem.security;

import com.example.acmeplex.usersystem.model.RegisteredUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserInfoDetails implements UserDetails {
    private String username;
    private String password;

    public UserInfoDetails(RegisteredUser registeredUser) {
        this.username = registeredUser.getEmail();
        this.password = registeredUser.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
