package org.mcs.mcsproductservice.security.token.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Token {
    private UUID id;
    private String subject;
    private List<String> authorities;
    private Date createdAt;
    private Date expiresAt;
}
