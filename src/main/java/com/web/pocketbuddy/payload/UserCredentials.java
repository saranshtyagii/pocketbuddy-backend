package com.web.pocketbuddy.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCredentials {

    private String usernameOrEmail;
    private String password;

}
