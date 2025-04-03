package com.web.pocketbuddy.service;

import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.dto.GroupExpensesDto;
import com.web.pocketbuddy.payload.GroupRegisterDetails;

import java.util.List;


public interface GroupExpenseService {

    GroupDetailsResponse createGroup(GroupRegisterDetails request);
    List<GroupDetailsResponse> findAllGroups(String userId);

    GroupDetailsResponse findGroupById(String groupId);
    List<GroupDetailsResponse> findGroupByName(String groupName);

    String deleteGroup(String userId, String groupId);
    GroupDetailsResponse updateGroup(String groupId);
    void deleteGroupFromDB(String apiKey, String groupId, String userId);
    GroupDetailsResponse recoverDeletedGroup(String userId, String groupId);

    GroupDetailsResponse joinGroup(String userId, String groupId);
    String leaveGroup(String userId, String groupId);



}
