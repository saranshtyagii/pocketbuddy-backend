package com.web.pocketbuddy.entity.utility;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NotNull
public class DeviceDetail {

    private Date loginDate;
    private String loginIp;
    private String loginDeviceId;
    private String modelName;
    private String modelVersion;
    private String osVersion;
    private String authToken;

}
