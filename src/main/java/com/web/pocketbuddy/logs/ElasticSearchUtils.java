package com.web.pocketbuddy.logs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@Getter
@ToString
public class ElasticSearchUtils {

    private String userId;
    private LogsConstraints description;
    private String sourcePlatform;
    private String currentAppVersion;
    private String address;
    private String ipAddress;
    private String mobileNumber;
    private String deviceName;
    private boolean ios;
    private boolean webRequest;


    public static void push(ElasticSearchUtils elasticSearchUtils, String pushIndex) {

        if(pushIndex.isBlank()) {
            pushException(ElasticSearchUtils.builder().build(), new NullPointerException("Push index is blank"));
            return;
        }
        if(!pushIndex.equals(LogsConstraints.POCKET_BUDDY_WEB_LOGS.toString()) ||
                !pushIndex.equals(LogsConstraints.POCKET_BUDDY_WEB_LOGS.toString())) {
            pushException(ElasticSearchUtils.builder().build(), new NullPointerException("Push index ("+pushIndex+") is incorrect"));
            return;
        }
        System.out.println(elasticSearchUtils.toString());
    }

    public static void pushException(ElasticSearchUtils elasticSearchUtils, Exception exception) {
        System.err.println(exception.getMessage());
    }

}
