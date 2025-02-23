package com.web.pocketbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class GroupExpensesDto {

    private String expenseId;
    private String groupId;
    private String description;
    private double amount;
    private Date createDate;
    private Date lastUpdateDate;
    private boolean isUpdated;
    private String paidByUserId;
    private List<UserDetailResponse> includedMembers;

}
