package com.web.pocketbuddy.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterUser {

    private String username;
    private String email;
    private String mobileNumber;
    private String password;

}
