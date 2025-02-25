package com.web.pocketbuddy.entity.document;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@ToString
public class Config {

    public Config(boolean crmEnabled, String crmAccessToken, String jwtSecretKey) {
        this.crmEnabled = crmEnabled;
        this.crmAccessToken = crmAccessToken;
        this.jwtSecretKey = jwtSecretKey;
    }

    @Id
    private String id;
    private boolean crmEnabled;
    private String crmAccessToken;
    private String jwtSecretKey;

}
