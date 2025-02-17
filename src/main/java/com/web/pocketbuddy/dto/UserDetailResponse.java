package com.web.pocketbuddy.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class UserDetailResponse {

    private String userId;
    private String userFirstName;
    private String userLastName;
    private String username;
    private String email;
    private String mobileNumber;

    private double userMonthlyBudget;
    private double userMonthlyExpense;
    private double userMonthlyIncome;

    private double userYearlyBudget;
    private double userYearlyExpense;
    private double userYearlyIncome;

}
