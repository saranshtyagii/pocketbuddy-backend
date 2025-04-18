package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.dto.GroupExpenseDto;
import com.web.pocketbuddy.entity.dao.GroupExpenseMasterDao;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.GroupExpenseDocument;
import com.web.pocketbuddy.entity.helper.GroupExpenseMetaData;
import com.web.pocketbuddy.exception.GroupApiExceptions;
import com.web.pocketbuddy.payload.RegisterGroupExpense;
import com.web.pocketbuddy.service.GroupDetailsService;
import com.web.pocketbuddy.service.GroupExpenseService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import com.web.pocketbuddy.utils.ConfigService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@AllArgsConstructor
public class GroupExpenseResponseService implements GroupExpenseService {

    private final GroupDetailsService groupDetailsService;
    private final GroupExpenseMasterDao groupExpenseMasterDao;


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
        GroupExpenseDocument savedExpense = groupExpenseMasterDao.save(request);

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
    public String deleteExpenseFromDb(String expenseId, String apiKey) {

        if(!apiKey.equals(ConfigService.getConfig().getApiKey())) {
            throw new GroupApiExceptions("Invalid Api Key", HttpStatus.FORBIDDEN);
        }

        if(StringUtils.isNotBlank(expenseId)) {
            groupExpenseMasterDao.delete(findExpenseDocumentById(expenseId));
            return "Deleted expense " + expenseId;
        }

        List<GroupExpenseDocument> savedGroupExpense = groupExpenseMasterDao.findAll();

        savedGroupExpense.parallelStream().forEach(savedGroupExpenseDoc -> {
            if(savedGroupExpenseDoc.isDeleted()) {
                groupExpenseMasterDao.delete(savedGroupExpenseDoc);
            }
        });

        return "All expenses deleted which marked as deleted";
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
        return groupExpenseMasterDao.findByGroupId(groupId);
    }

    private GroupExpenseDocument findExpenseDocumentById(String expenseId) {
        return groupExpenseMasterDao.findByExpenseId(expenseId)
                .orElseThrow(() -> new GroupApiExceptions("No expense found!", HttpStatus.NOT_FOUND));
    }

}
