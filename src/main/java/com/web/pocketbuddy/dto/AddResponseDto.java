package com.web.pocketbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class AddResponseDto {

    private String screen;
    private String placement;
    private String addUrl;
    private boolean success;

}
