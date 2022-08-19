package com.busra.connecting.config.security;

import com.busra.connecting.config.security.firebase.FirebaseParser;
import com.busra.connecting.model.ObjectId;
import com.busra.connecting.model.User;
import com.busra.connecting.service.UserService;
import com.google.firebase.auth.FirebaseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
public class CusAuthConv implements ServerAuthenticationConverter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userDetailsService;
    private final FirebaseParser firebaseParser;
    private final List<String> kil = Arrays.asList("yahoo","gmail","hotmail");
    private String authToken, username, email, ipAddress;
    private boolean isApplied;

    public CusAuthConv(@Qualifier("userService") UserService userDetailsService, FirebaseParser firebaseParser) {
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");
        Assert.notNull(firebaseParser, "userDetailsService cannot be null");

        this.userDetailsService = userDetailsService;
        this.firebaseParser = firebaseParser;
    }

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.param}")
    private String tokenParam;

    @Value("${jwt.prefix}")
    private String bearerPrefix;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) throws BadCredentialsException {
            return Mono.justOrEmpty(exchange).map(ServerWebExchange::getRequest)
                            .flatMap(httpRequest -> {
                                this.email = httpRequest.getQueryParams().get("email") != null ? httpRequest.getQueryParams().get("email").get(0) : "";
                                this.username = httpRequest.getQueryParams().get("name")!=null?httpRequest.getQueryParams().get("name").get(0):"";
                                this.ipAddress = Objects.requireNonNull(httpRequest.getRemoteAddress()).getAddress().getHostAddress();
                                this.isApplied =  httpRequest.getPath().value().equals("/api/rest/news/save");
                                return Mono.justOrEmpty(httpRequest.getHeaders().getFirst(tokenHeader));
                            })
                            .filter(header -> header.trim().startsWith(bearerPrefix))
                            .map(header -> header.substring(bearerPrefix.length() + 1))
                            .flatMap(token -> {
                                this.authToken = token;
                                return firebaseParser.parseToken(token);
                            })
                            .map(firebaseTokens->getUserObject(User.of(), firebaseTokens, email, ipAddress, username))
                            .flatMap(user -> this.getUserMono(user, this.isApplied, ipAddress))
                            .map(user -> new JwtAuthenticationToken(this.authToken, user, this.authToken, user.getAuthorities()));
    }

    private Mono<User> getUserMono(User user, boolean isApply, String ipAddress) {
        return this.userDetailsService.findById(user.getId().toHexString())
                .flatMap(user1 -> {
                    if (user1.getEmail()==null) {
                        return userDetailsService.save(user);
                    } else if (isApply && !user.getIpAddress().contains(ipAddress)){
                        List<String> list=user1.getIpAddress();
                        list.add(ipAddress);
                        return userDetailsService.save(User.from(user).withIpAddress(list).build());
                    }
                    if (!user1.isEnabled())
                        return Mono.empty();
                    else
                        return Mono.just(user1);
                });
    }

    private User getUserObject(User.Builder builder, FirebaseToken x, String email, String ipAddress, String username) {
        return builder.withId(new ObjectId(x.getUid().substring(0,12).getBytes()))
                .withEmail(x.getEmail()!=null ? x.getEmail():email)
                .withContentsCount(0L)
                .withDate(new Date())
                .withFirstname(x.getName())
                .withLastname(x.getName())
                .withRoles(Collections.singletonList("ROLE_ADMIN"))
                .withFollowers(Collections.emptyList())
                .withUsers(Collections.emptyList())
                .withTags(Collections.emptyList())
                .withMediaParts(Collections.emptyList())
                .withPassword(x.getUid())
                .withUsername(getUserName(x.getEmail()!=null ? x.getEmail():email))
                .withSummary("Lütfen Kısa Özet Giriniz")
                .withEnabled(true)
                .withIban("")
                .withIpAddress(Collections.singletonList(ipAddress))
                .withBlocked(Collections.emptyList())
                .withImage("/assets/profile-img.jpeg")
                .build();
    }
    private String getUserName(String emailx){
        if (!emailx.equals("")) {
            String[] list = emailx.split("@");
            int il=kil.indexOf(list[1].split("\\.")[0]);
            return list[0] + (il != -1 ? il:4);
        }
        return "";
    }
}
