package org.mcs.mcsproductservice.security.token.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RefreshVerifyConfig {

    @Value("${jwt.refresh-secret}")
    private String refreshTokenSecret;

    private final JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;

    @Bean(name = "refreshConfigBean")
    public String cryptsSecret() throws JOSEException {
        return new OctetSequenceKeyGenerator(256)
                .keyID(refreshTokenSecret)
                .algorithm(jwsAlgorithm)
                .generate()
                .toJSONString();
    }
}
