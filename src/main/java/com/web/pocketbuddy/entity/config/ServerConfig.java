package com.web.pocketbuddy.entity.config;

public class ServerConfig {

    private static ServerConfig serverConfig;

    private ServerConfig() {}

    public static ServerConfig getInstance() {
        if (serverConfig == null) {
            serverConfig = new ServerConfig();
        }
        return serverConfig;
    }

    private static String jwtTokenSecretKey = "";


}
