package com.web.pocketbuddy.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddPersonalExpense {

    private String userID;
    private String expenseID;
    private String description;
    private double amount;

}
