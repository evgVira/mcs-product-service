package org.mcs.mcsproductservice.security.token.tokenFactory;

import org.mcs.mcsproductservice.security.token.model.Token;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Component
public class RefreshTokenFactory implements Function<UserDetails, Token> {

    private Duration tokenTtl = Duration.ofDays(1);

    @Override
    public Token apply(UserDetails userDetails) {
        List<String> authorities = new ArrayList<>();

        authorities.add("JWT_REFRESH");
        authorities.add("JWT_LOGOUT");

        userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map("GRANT_%s"::formatted)
                .forEach(authorities::add);

        Date createdAt = Date.from(Instant.now());
        Date expiresAt = new Date(createdAt.getTime() + tokenTtl.toMillis());

        return Token.builder()
                .id(UUID.randomUUID())
                .subject(userDetails.getUsername())
                .authorities(authorities)
                .createdAt(createdAt)
                .expiresAt(expiresAt)
                .build();
    }
}
