package com.web.pocketbuddy.payload;

import lombok.Getter;

@Getter
public class FetchByDates {

    private String userID;
    private String roomId;
    private String startDate;
    private String endDate;

}
