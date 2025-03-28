package com.web.pocketbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@AllArgsConstructor
@Getter
public class PersonalExpenseResponse {

    private String expenseId;
    private String description;
    private double amount;
    private Date expenseDate;
    private Date updatedDate;
    private boolean isEdited;
    private boolean isDeleted;

}
