package com.tumi.haul.security.jwt;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
@Data
@Builder
public class JwtResp implements Serializable {
    private static final long  serialVersionUID=-8091879091924046844L;
    private final String jwtToken;
    @Getter
    private final String refreshToken;
    private final String tokenId;

    public JwtResp(String jwtToken, String refreshToken, String tokenId) {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
        this.tokenId = tokenId;
    }
}
