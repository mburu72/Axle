package com.tumi.haul.model.refresh;

import lombok.Data;

@Data
public class RefreshTokenReq {
    public String token;
    public String tokenId;
}
