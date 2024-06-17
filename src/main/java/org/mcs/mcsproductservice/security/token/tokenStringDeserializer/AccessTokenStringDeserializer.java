package org.mcs.mcsproductservice.security.token.tokenStringDeserializer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mcs.mcsproductservice.security.token.config.AccessVerifyConfig;
import org.mcs.mcsproductservice.security.token.model.Token;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Qualifier("accessConfigBean")
@Slf4j
public class AccessTokenStringDeserializer implements Function<String, Token> {

    private final AccessVerifyConfig accessVerifyConfig;

    @Override
    public Token apply(String string) {
        try {

            MACVerifier macVerifier = new MACVerifier(accessVerifyConfig.cryptSecret());

            SignedJWT signedJWT = SignedJWT.parse(string);

            if (signedJWT.verify(macVerifier)) {
                JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

                return Token.builder()
                        .id(UUID.fromString(jwtClaimsSet.getJWTID()))
                        .subject(jwtClaimsSet.getSubject())
                        .authorities(jwtClaimsSet.getStringListClaim("authorities"))
                        .createdAt(jwtClaimsSet.getIssueTime())
                        .expiresAt(jwtClaimsSet.getExpirationTime())
                        .build();
            }
        } catch (JOSEException | ParseException exception) {
            log.error(exception.getMessage());
        }
        return null;
    }
}
