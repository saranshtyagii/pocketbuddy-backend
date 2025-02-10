package com.web.pocketbuddy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
    private String token;

    public TokenResponse(String authToken) {
        this.token = authToken;
    }
}
