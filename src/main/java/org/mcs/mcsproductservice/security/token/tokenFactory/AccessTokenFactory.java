package org.mcs.mcsproductservice.security.token.tokenFactory;

import org.mcs.mcsproductservice.security.token.model.Token;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class AccessTokenFactory implements Function<Token, Token> {

    private Duration tokenTtl = Duration.ofMinutes(5);

    @Override
    public Token apply(Token token) {
        List<String> authorities = token.getAuthorities().stream()
                .filter(authority -> authority.startsWith("GRANT_"))
                .map(authority -> authority.replace("GRANT_", ""))
                .toList();

        Date createdAt = Date.from(Instant.now());
        Date expiresAt = new Date(createdAt.getTime() + tokenTtl.toMillis());

        return Token.builder()
                .id(token.getId())
                .subject(token.getSubject())
                .authorities(authorities)
                .createdAt(createdAt)
                .expiresAt(expiresAt)
                .build();
    }
}
