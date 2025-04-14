package com.web.pocketbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupDetailsResponse {

    private String groupId;
    private String groupName;
    private String groupDescription;

    private String createdByUserId;
    private Date createdAt;

    private Map<String, Double> joinedMembers;

}
