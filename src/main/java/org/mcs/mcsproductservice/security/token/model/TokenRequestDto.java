package org.mcs.mcsproductservice.security.token.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenRequestDto {

    private String username;
    private String password;
}
