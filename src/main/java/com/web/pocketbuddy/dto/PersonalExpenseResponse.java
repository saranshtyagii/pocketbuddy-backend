package com.web.pocketbuddy.dto;

import lombok.*;
import lombok.Getter;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalExpenseResponse {

    private String expenseId;
    private String description;
    private double amount;
    private Date expenseDate;
    private Date updatedDate;
    private boolean isEdited;
    private boolean isDeleted;

}
