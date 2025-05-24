package com.web.pocketbuddy.entity.helper;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DeviceDetail {

    private Date loginDate;
    private String loginIp;
    private String loginDeviceId;
    private String modelName;
    private String modelVersion;
    private String osVersion;
    private String authToken;

}
