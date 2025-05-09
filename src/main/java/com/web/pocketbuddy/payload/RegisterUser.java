package com.web.pocketbuddy.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RegisterUser {

    @NotNull
    private String userFirstName;
    private String userLastName;

    @Email
    private String email;
    private String mobileNumber;
    @NotNull
    private String password;

}
