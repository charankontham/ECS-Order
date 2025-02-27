package com.ecs.ecs_order.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {
    private final String username;
    private final String password;
    private final String role;

    public UserPrincipal(CustomerDto customerDto) {
        this.username = customerDto.getEmail();
        this.password = customerDto.getPassword();
        this.role = "ROLE_" + customerDto.getRole().toUpperCase();
    }

    public UserPrincipal(AdminDto adminDto) {
        this.username = adminDto.getAdminUsername();
        this.password = adminDto.getAdminPassword();
        this.role = "ROLE_" + (adminDto.getAdminRole().getSubRole() + "_" +
                adminDto.getAdminRole().getSubRole()).toUpperCase();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
