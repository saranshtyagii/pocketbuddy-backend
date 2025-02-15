package com.web.pocketbuddy.service;

import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.payload.GroupRegisterDetails;

import java.util.List;

public interface GroupExpenseService {

    public GroupDetailsResponse registerGroup(GroupRegisterDetails registerDetails);
    public GroupDetailsResponse getGroupDetails(String groupId);
    public GroupDetailsResponse updateGroupDetails(String groupId, GroupDetailsResponse groupDetailsResponse);
    public GroupDetailsResponse deleteGroup(String groupId);

    public List<GroupDetailsResponse> getAllGroups(String userId);




}
