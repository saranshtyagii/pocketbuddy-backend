package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.dto.AddResponseDto;

public class AddServiceResponse {

    public static AddResponseDto fetchAddForUser(String screen, String placement) {
        return AddResponseDto.builder()
                .screen(screen)
                .placement(placement)
                .addUrl(null)
                .success(false)
                .build();
    }

}
