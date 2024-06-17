package org.mcs.mcsproductservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mcs.mcsproductservice.security.UserEntityService;
import org.mcs.mcsproductservice.security.token.model.Token;
import org.mcs.mcsproductservice.security.token.model.TokenRequestDto;
import org.mcs.mcsproductservice.security.token.model.TokensResponse;
import org.mcs.mcsproductservice.security.token.tokenFactory.AccessTokenFactory;
import org.mcs.mcsproductservice.security.token.tokenFactory.RefreshTokenFactory;
import org.mcs.mcsproductservice.security.token.tokenStringSerializer.AccessTokenStringSerializer;
import org.mcs.mcsproductservice.security.token.tokenStringSerializer.RefreshTokenStringSerializer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateTokenServiceServiceImpl implements CreateTokenService {

    private final UserEntityService userEntityService;

    private final RefreshTokenFactory refreshTokenFactory;

    private final AccessTokenFactory accessTokenFactory;

    private final AccessTokenStringSerializer accessTokenStringSerializer;

    private final RefreshTokenStringSerializer refreshTokenStringSerializer;

    private final PasswordEncoder passwordEncoder;

    @Override
    public TokensResponse createToken(TokenRequestDto tokenRequestDto) {

        String username = tokenRequestDto.getUsername();
        UserDetails userDetails = userEntityService.loadUserByUsername(username);

        if(tokenRequestDto.getUsername().equals(userDetails.getUsername()) && passwordEncoder.matches(tokenRequestDto.getPassword(), userDetails.getPassword())){

            Token refreshToken = refreshTokenFactory.apply(userDetails);
            Token accessToken = accessTokenFactory.apply(refreshToken);
            String accessTokenToString = accessTokenStringSerializer.apply(accessToken);
            String refreshTokenToString = refreshTokenStringSerializer.apply(refreshToken);

            return TokensResponse.builder()
                    .accessToken(accessTokenToString)
                    .refreshToken(refreshTokenToString)
                    .build();
        }
        log.error("User must be authenticated");
        return null;
    }
}
