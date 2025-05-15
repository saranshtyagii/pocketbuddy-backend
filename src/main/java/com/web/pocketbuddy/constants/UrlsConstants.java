package com.web.pocketbuddy.constants;

import org.springframework.beans.factory.annotation.Value;

public class UrlsConstants {

    private UrlsConstants() {}

    @Value("server.port")
    private static String port;
    public static final String HOST_HTTP_BASE_URL = "http://192.168.1.4:"+port;
    public static final String HOST_URL = "/pocketbuddy";
    public static final String BASE_URL_V1 = HOST_URL + "/api/v1";

    public static final String AUTH_URL = BASE_URL_V1 + "/auth";
    public static final String USER_URL = BASE_URL_V1 + "/user";
    public static final String PERSONAL_URL = BASE_URL_V1 + "/personal";
    public static final String GROUP_URL = BASE_URL_V1 + "/group";
    public static final String GROUP_EXPENSE_URL = BASE_URL_V1 + "/group-expenses";
    public static final String CRM_URL = BASE_URL_V1 + "/crm";
    public static final String ADD_MOD_URL = "/addmod/fetch/addmod";

}
