package com.busra.connecting.config.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final ReactiveUserDetailsService userDetailsService;

    public CustomReactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication);
    }

}
