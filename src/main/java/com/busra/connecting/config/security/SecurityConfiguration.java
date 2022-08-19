package com.busra.connecting.config.security;

import com.busra.connecting.model.ObjectId;
import com.busra.connecting.service.UserService;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.util.Assert;


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    private final ServerAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();

    private final UserService userDetailsService;
    private final CusAuthConv cusAuthConv;

    public SecurityConfiguration(UserService userDetailsService, CusAuthConv cusAuthConv) {
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");
        Assert.notNull(cusAuthConv, "customAuthenticationConverter cannot be null");
        this.userDetailsService = userDetailsService;
        this.cusAuthConv = cusAuthConv;
    }

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        // Disable default security.
        http.httpBasic().disable();
        http.formLogin().disable();
        http.csrf().disable();
        http.logout().disable();

        // Add custom security.
        http.authenticationManager(authenticationManager());
        // Disable authentication for `/resources/**` routes.
       // http.authorizeExchange().pathMatchers("/resources/**").permitAll();
       // http.authorizeExchange().pathMatchers("/webjars/**").permitAll();

        //Disable authentication for `/test/**` routes.
       // http.authorizeExchange().pathMatchers("/test/**").permitAll();

        // Disable authentication for `/auth/**` routes.
       // http.authorizeExchange().pathMatchers("/auth/**").permitAll();

        // Access control for profile pages
        http.authorizeExchange().pathMatchers("/api/rest/user/{username}/{random}").access((authentication, context) ->
        authentication
            .map(auth -> auth.getName().equals(context.getVariables().get("username").toString()))
           // .map(username -> username.equals(context.getVariables().get("username").toString()))
            .map(AuthorizationDecision::new)
        ); // username.equals((new ObjectId(context.getVariables().get("username").toString().substring(0,12).getBytes())).toHexString()))
        http.authorizeExchange().pathMatchers(HttpMethod.DELETE).access((authentication, context) ->
                authentication
                        .map(auth -> ((UserDetails)auth.getPrincipal()).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
                       // .map(authorities -> ((UserDetails)authorities).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .map(AuthorizationDecision::new)
        );
        http.authorizeExchange().pathMatchers(HttpMethod.PUT).access((authentication, context) ->
                authentication
                        .map(auth -> ((UserDetails)auth.getPrincipal()).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))||((UserDetails)auth.getPrincipal()).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")))
                       // .map(authorities -> authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))||authorities.contains(new SimpleGrantedAuthority("ROLE_USER")))
                        .map(AuthorizationDecision::new)
        );
       // http.securityContextRepository(securityContextRepository());
        http.authorizeExchange().pathMatchers(HttpMethod.GET,"/**").permitAll();

        http.authorizeExchange().anyExchange().authenticated();
        //.and().httpBasic().disable();

        http.addFilterAt(apiAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
        // .httpBasic().disable().csrf().disable();

        return http.build();
    }

    private ReactiveAuthenticationManager authenticationManager() {
        return new CustomReactiveAuthenticationManager(this.userDetailsService);
    }

    private AuthenticationWebFilter apiAuthenticationWebFilter() {
        try {
            AuthenticationWebFilter apiAuthenticationWebFilter = new AuthenticationWebFilter(authenticationManager());
            apiAuthenticationWebFilter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(this.entryPoint));
            apiAuthenticationWebFilter.setServerAuthenticationConverter(this.cusAuthConv);
            OrServerWebExchangeMatcher matcher= new OrServerWebExchangeMatcher(
                    new PathPatternParserServerWebExchangeMatcher("/**", HttpMethod.POST),
                    new PathPatternParserServerWebExchangeMatcher("/**", HttpMethod.PUT),
                    new PathPatternParserServerWebExchangeMatcher("/**", HttpMethod.PATCH),
                    new PathPatternParserServerWebExchangeMatcher("/**", HttpMethod.DELETE),
                    new PathPatternParserServerWebExchangeMatcher("/api/rest/user/**", HttpMethod.GET));
            apiAuthenticationWebFilter.setRequiresAuthenticationMatcher(matcher);

            // Setting the Context Repo helped, not sure if I need this
           // apiAuthenticationWebFilter.setSecurityContextRepository(securityContextRepository());

            return apiAuthenticationWebFilter;
        } catch (Exception e) {
            throw new BeanInitializationException("Could not initialize AuthenticationWebFilter apiAuthenticationWebFilter.", e);
        }
    }

}
