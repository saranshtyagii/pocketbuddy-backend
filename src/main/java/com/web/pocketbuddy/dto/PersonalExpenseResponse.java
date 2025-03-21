package com.web.pocketbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Date;

@Builder
@AllArgsConstructor
public class PersonalResponseResponse {

    private String expenseId;
    private String description;
    private double amount;
    private Date expenseDate;
    private Date updatedDate;
    private boolean isEdited;
    private boolean isDeleted;

}
