package org.mcs.mcsproductservice.controller;

import lombok.RequiredArgsConstructor;
import org.mcs.mcsproductservice.security.token.model.TokenRequestDto;
import org.mcs.mcsproductservice.security.token.model.TokensResponse;
import org.mcs.mcsproductservice.service.CreateTokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final CreateTokenService createTokenService;

    @PostMapping("/token/create")
    public TokensResponse createToken(@RequestBody TokenRequestDto tokenRequestDto){
        return createTokenService.createToken(tokenRequestDto);
    }
}
