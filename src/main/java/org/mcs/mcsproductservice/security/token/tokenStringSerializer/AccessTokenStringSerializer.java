package org.mcs.mcsproductservice.security.token.tokenStringSerializer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mcs.mcsproductservice.security.token.config.AccessVerifyConfig;
import org.mcs.mcsproductservice.security.token.model.Token;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
@Qualifier("accessConfigBean")
public class AccessTokenStringSerializer implements Function<Token, String> {

    private final AccessVerifyConfig accessVerifyConfig;

    @Override
    public String apply(Token token) {

        JWSHeader jwsHeader = new JWSHeader.Builder(accessVerifyConfig.getJwsAlgorithm())
                .keyID(token.getId().toString())
                .build();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.getId().toString())
                .subject(token.getSubject())
                .claim("authorities", token.getAuthorities())
                .issueTime(token.getCreatedAt())
                .expirationTime(token.getExpiresAt())
                .build();

        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);

        try {
            MACSigner macSigner = new MACSigner(accessVerifyConfig.cryptSecret());
            signedJWT.sign(macSigner);
            String accessTokenToString = signedJWT.serialize();

            return accessTokenToString;
        } catch (JOSEException exception) {
            log.error(exception.getMessage());
        }
        return null;
    }
}
