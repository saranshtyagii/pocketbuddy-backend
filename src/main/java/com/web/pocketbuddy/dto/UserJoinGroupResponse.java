package com.web.pocketbuddy.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserJoinGroupResponse {

    private String groupId;
    private String groupName;
    private String description;

    private Date createdAt;
    private Date updatedAt;

    private String createdByUserID;
    private String createdByUserName;

    private Map<String, String> membersDetails;

}
