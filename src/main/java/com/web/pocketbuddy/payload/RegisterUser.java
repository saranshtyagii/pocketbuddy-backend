package com.web.pocketbuddy.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterUser {

    @NotNull
    private String userFirstName;
    private String userLastName;

    @NotNull
    private String username;
    @Email
    private String email;
    private String mobileNumber;
    @NotNull
    private String password;

}
