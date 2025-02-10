package com.web.pocketbuddy.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCredentials {

    private String usernameOrEmail;
    private String password;
//    private String deviceId;
//    private String ipAddress;
//    private String modelName;
//    private String modelVersion;
//    private String osVersion;
//    private String appVersion;

}
