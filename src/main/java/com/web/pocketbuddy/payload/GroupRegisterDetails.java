package com.web.pocketbuddy.payload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupRegisterDetails {

    private String groupName;
    private String description;

    private String createdByUser;

    private double groupBudget;
    private double budgetPerDay;

    private Date tripStartDate;
    private Date tripEndDate;

}

