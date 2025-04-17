package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.dto.GroupExpenseDto;
import com.web.pocketbuddy.entity.dao.GroupExpenseMasterDoa;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.GroupExpenseDocument;
import com.web.pocketbuddy.entity.helper.GroupExpenseMetaData;
import com.web.pocketbuddy.payload.RegisterGroupExpense;
import com.web.pocketbuddy.service.GroupDetailsService;
import com.web.pocketbuddy.service.GroupExpenseService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@AllArgsConstructor
public class GroupExpenseResponseService implements GroupExpenseService {

    private final GroupDetailsService groupDetailsService;
    private final GroupExpenseMasterDoa groupExpenseMasterDoa;


    @Override
    public GroupExpenseDto addExpense(RegisterGroupExpense expensePayload) {
        GroupDocument savedGroup = groupDetailsService.findGroupDocumentById(expensePayload.getGroupId());
        GroupExpenseDocument request = MapperUtils.convertToGroupExpenseDocument(expensePayload);

        // Get existing member expenses or initialize a new one
        Map<String, Double> groupExpenses = savedGroup.getMembers();
        if (groupExpenses == null) {
            groupExpenses = new HashMap<>();
        }

        // Update group expenses based on included members
        for (Map.Entry<String, GroupExpenseMetaData> entry : request.getIncludedMembers().entrySet()) {
            String userId = entry.getKey();
            Double addedAmount = entry.getValue().getAmount();

            groupExpenses.put(userId, groupExpenses.getOrDefault(userId, 0.0) + addedAmount);
        }

        // Save updated expenses back to the group
        savedGroup.setMembers(groupExpenses);
        groupDetailsService.saveOrUpdate(savedGroup);

        // Save the expense entry (you may already have a service/repository for this)
        GroupExpenseDocument savedExpense = groupExpenseMasterDoa.save(request);

        return MapperUtils.convertToGroupExpenseDto(savedExpense);
    }


    @Override
    public GroupExpenseDto updateExpense(RegisterGroupExpense expensePayload) {
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
    public List<GroupExpenseDto> fetchGroupExpenseByGroupId(String groupId) {
        List<GroupExpenseDocument> savedExpenses = findGroupExpenseDocumentByGroupId(groupId);
        if(CollectionUtils.isEmpty(savedExpenses)) {
            return List.of();
        }
        return savedExpenses.stream()
                .filter(expense -> !expense.isDeleted())
                .map(MapperUtils::convertToGroupExpenseDto).toList();
    }

    private List<GroupExpenseDocument> findGroupExpenseDocumentByGroupId(String groupId) {
        return groupExpenseMasterDoa.findByGroupId(groupId).orElse(Collections.emptyList());
    }

}
