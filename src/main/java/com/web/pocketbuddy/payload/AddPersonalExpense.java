package com.web.pocketbuddy.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AddPersonalExpense {

    private String userID;
    private String expenseID;
    private String description;
    private double amount;
    private Date createdDate;

}
