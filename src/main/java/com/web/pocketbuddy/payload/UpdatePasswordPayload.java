package com.web.pocketbuddy.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordPayload {

    private String token;
    private String newPassword;

}
