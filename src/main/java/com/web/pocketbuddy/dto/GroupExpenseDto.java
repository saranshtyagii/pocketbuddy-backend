package com.web.pocketbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class GroupExpenseDto {

    private String groupId;
    private String expenseId;
    private String userId;
    private String description;
    private Double amount;

    private Date createdAt;
    private boolean edited;

    private Map<String, Map<String,Double>> includedMembers;

}
