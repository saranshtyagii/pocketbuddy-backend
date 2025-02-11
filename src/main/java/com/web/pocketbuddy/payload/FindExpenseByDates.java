package com.web.pocketbuddy.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FindExpenseByDates {
    private String userID;
    private Date startDate;
    private Date endDate;
}
