package com.busra.connecting.model;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

    private String id;

    @Override
    public String getAuthority() {
        return id;
    }
}
