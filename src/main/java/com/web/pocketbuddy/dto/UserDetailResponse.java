package com.web.pocketbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.util.List;

@Builder
@AllArgsConstructor
public class UserDetailResponse {

    private String userId;
    private String username;
    private String email;
    private String mobileNumber;

    private List<UserJoinGroupResponse> personalExpense;

}
