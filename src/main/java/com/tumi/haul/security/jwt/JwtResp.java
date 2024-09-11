package com.tumi.haul.security.jwt;

import java.io.Serializable;

public class JwtResp implements Serializable {
    private static final long  serialVersionUID=-8091879091924046844L;
    private final String jwttoken;

    public JwtResp(String jwttoken) {
        this.jwttoken = jwttoken;
    }
    public String getToken(){
        return this.jwttoken;
    }
}
