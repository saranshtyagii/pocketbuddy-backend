package com.web.pocketbuddy.service;

import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.dto.GroupExpensesDto;
import com.web.pocketbuddy.entity.document.GroupExpenseDocument;
import com.web.pocketbuddy.payload.GroupRegisterDetails;

import java.util.List;

public interface GroupExpenseService {

    public GroupDetailsResponse registerGroup(GroupRegisterDetails registerDetails);
    public GroupDetailsResponse getGroupDetails(String groupId);
    public GroupDetailsResponse updateGroupDetails(String groupId, GroupDetailsResponse groupDetailsResponse);
    public String deleteGroup(String groupId, String userId);
    public GroupDetailsResponse joinGroup(String groupId, String userId);

    public List<GroupDetailsResponse> getAllGroups(String userId);

    public GroupDetailsResponse findGroupById(String groupId);

    public String deleteGroupFromDb(String apiKey);

    public List<GroupExpensesDto> findGroupExpensesByGroupId(String groupId);

    String markExpenseAsDeleted(String expenseId, String userId);

    List<GroupExpensesDto> addExpense(GroupExpensesDto groupExpensesDto);
}
