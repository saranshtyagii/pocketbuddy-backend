package com.web.pocketbuddy.entity.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Builder
public class Config {

    @Id
    private String id;
    private boolean crmEnabled;
    private String crmAccessToken;
    private String jwtSecretKey;
    private String apiKey;
    private String adminPassword;
    private boolean validateConnection;

}
