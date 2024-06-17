package org.mcs.mcsproductservice.service;

import org.mcs.mcsproductservice.security.token.model.TokenRequestDto;
import org.mcs.mcsproductservice.security.token.model.TokensResponse;

public interface CreateTokenService {
    TokensResponse createToken(TokenRequestDto tokenRequestDto);
}
