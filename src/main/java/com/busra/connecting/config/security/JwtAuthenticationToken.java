package com.busra.connecting.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private static String username;
    private static String token;
    private static UserDetails user;

    public JwtAuthenticationToken(String email, UserDetails user, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        username=email;
        JwtAuthenticationToken.token = token;
        JwtAuthenticationToken.user =user;
    }
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        JwtAuthenticationToken.token = token;
    }
    @Override
    public boolean isAuthenticated() {
        return true;
    }
    @Override
    public Object getCredentials() {
        return token;
    }
	@Override
	public UserDetails getPrincipal()
	{
		return user;
	}
    @Override
    public String getName() {
        return username;
    }

}
