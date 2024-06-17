package org.mcs.mcsproductservice.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mcs.mcsproductservice.security.token.model.Token;
import org.mcs.mcsproductservice.security.token.tokenStringDeserializer.AccessTokenStringDeserializer;
import org.mcs.mcsproductservice.security.token.tokenStringDeserializer.RefreshTokenStringDeserializer;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationConverter implements AuthenticationConverter {

    private final AccessTokenStringDeserializer accessTokenStringDeserializer;

    private final RefreshTokenStringDeserializer refreshTokenStringDeserializer;

    @Override
    public Authentication convert(HttpServletRequest request)  {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);echo "# mcs-product-service" >> README.md
        git init
        git add README.md
        git commit -m "first commit"
        git branch -M main
        git remote add origin git@github.com:evgVira/mcs-product-service.git
        git push -u origin main

        if (header != null && header.startsWith("Bearer ")) {
            String requestToken = header.replace("Bearer ", "");

            Token accessToken = accessTokenStringDeserializer.apply(requestToken);

            if (accessToken != null) {
                return new PreAuthenticatedAuthenticationToken(accessToken, requestToken);
            }

            Token refreshToken = refreshTokenStringDeserializer.apply(requestToken);

            if (refreshToken != null) {
                return new PreAuthenticatedAuthenticationToken(refreshToken, requestToken);
            }
        }
        log.error("User must be authenticated");
        return null;
    }
}
