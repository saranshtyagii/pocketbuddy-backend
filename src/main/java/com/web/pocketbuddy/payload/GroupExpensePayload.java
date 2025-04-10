package com.web.pocketbuddy.payload;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class GroupExpensePayload {

    private String groupId;
    private String userId;
    private String description;
    private double amount;
    private List<String> includedMembers;

}
