package com.web.pocketbuddy.service;

import com.web.pocketbuddy.dto.GroupExpenseDto;
import com.web.pocketbuddy.payload.FindExpenseByDates;
import com.web.pocketbuddy.payload.RegisterGroupExpense;

import java.util.List;
import java.util.Map;

public interface GroupExpenseService {

    public GroupExpenseDto addExpense(RegisterGroupExpense expensePayload);
    public GroupExpenseDto updateExpense(GroupExpenseDto expenseDto);
    public String MarkExpenseAsDeleted(String expenseId);
    public String deleteExpenseFromDb(String expenseId, String apiKey);
    public GroupExpenseDto getExpense(String expenseId);

    public List<GroupExpenseDto> fetchGroupExpenseByGroupId(String groupId);

    public List<GroupExpenseDto> fetchAllExpenseByDates(FindExpenseByDates details);

    public Map<String, Map<String, Double>> fetchOweAmount(String groupId);
    public Map<String, Map<String, Double>> fetchWhoPaidToWhom(String groupId);
}
