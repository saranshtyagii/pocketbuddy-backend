package com.web.pocketbuddy.constants;

import org.apache.commons.lang3.StringUtils;

public enum RedisTTL {

    ONE_MINUTES_SECONDS(60L),
    FIVE_MINUTES_SECONDS(5 * 60L),
    FIFTEEN_MINUTES_SECONDS(15 * 60L),
    THIRTY_MINUTES_SECONDS(30 * 60L),
    ONE_HOUR_SECONDS(60 * 60L),
    ONE_DAY_SECONDS(24 * 60 * 60L),
    TWO_DAY_SECONDS(2 * 24 * 60 * 60L),
    FOUR_DAY_SECONDS(4 * 24 * 60 * 60L),
    FIVE_DAY_SECONDS(5 * 24 * 60 * 60L),
    SEVEN_DAY_SECONDS(7 * 24 * 60 * 60L),
    TEN_DAY_SECONDS(10 * 24 * 60 * 60L);

    private final long ttl;

    RedisTTL(long ttl) {
        this.ttl = ttl;
    }

    public static RedisTTL getRedisTtl(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }

        try {
            return Enum.valueOf(RedisTTL.class, str.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

}
