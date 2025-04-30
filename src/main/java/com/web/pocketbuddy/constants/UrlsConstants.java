package com.web.pocketbuddy.constants;

public class UrlsConstants {

    private UrlsConstants() {}

    public static final String HOST_HTTP_BASE_URL = "http://172.20.10.6:8069";
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
