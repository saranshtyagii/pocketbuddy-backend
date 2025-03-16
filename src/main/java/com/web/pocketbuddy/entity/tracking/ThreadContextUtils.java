package com.web.pocketbuddy.entity.tracking;

import lombok.Getter;
import org.thymeleaf.util.StringUtils;

@Getter
public class ThreadContextUtils {

    private ThreadContextUtils() {}

    private static boolean loginIn;

    public static boolean isLoginIn() {
        return loginIn;
    }

    public static int getAppVersion() {
        return appVersion;
    }

    public static String getUserId() {
        return userId;
    }

    public static String getSource() {
        return source;
    }

    public static boolean isIos() {
        return ios;
    }

    public static boolean isWebRequest() {
        return webRequest;
    }

    public static String getIp() {
        return ip;
    }

    public static String getLocation() {
        return location;
    }

    private static int appVersion;
    private static String userId;
    private static String source;
    private static boolean ios;
    private static boolean webRequest;
    private static String ip;
    private static String location;

    public static void buildThread(boolean isLogIn, int appVersion, String userId, String source, String ip, String location) {
        ThreadContextUtils.loginIn = isLogIn;
        ThreadContextUtils.appVersion = appVersion;
        ThreadContextUtils.userId = userId;
        ThreadContextUtils.source = source;
        ThreadContextUtils.ios = StringUtils.equals(source, "pocketbuddy-ios");
        ThreadContextUtils.webRequest = StringUtils.equals(source, "pocketbuddy-web");
        ThreadContextUtils.ip = ip;
        ThreadContextUtils.location = location;
    }

}
