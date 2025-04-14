package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.dto.GroupExpenseDto;
import com.web.pocketbuddy.entity.dao.GroupExpenseMasterDoa;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.GroupExpenseDocument;
import com.web.pocketbuddy.payload.GroupExpensePayload;
import com.web.pocketbuddy.service.GroupDetailsService;
import com.web.pocketbuddy.service.GroupExpenseService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GroupExpenseResponseService implements GroupExpenseService {

    private final GroupDetailsService groupDetailsService;
    private final GroupExpenseMasterDoa groupExpenseMasterDoa;


    @Override
    public GroupExpenseDto addExpense(GroupExpensePayload expensePayload) {
        return null;
    }

    @Override
    public GroupExpenseDto updateExpense(GroupExpensePayload expensePayload) {
        return null;
    }

    @Override
    public String MarkExpenseAsDeleted(String expenseId) {
        return "";
    }

    @Override
    public String deleteExpense(String expenseId, String apiKey) {
        return "";
    }

    @Override
    public GroupExpenseDto getExpense(String expenseId) {
        return null;
    }

    @Override
    public List<GroupExpenseDto> getExpensesByGroupId(String groupId) {
        return List.of();
    }
}
