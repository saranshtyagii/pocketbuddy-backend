package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.dto.GroupExpenseDto;
import com.web.pocketbuddy.entity.dao.GroupExpenseMasterDao;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.GroupExpenseDocument;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.entity.helper.GroupExpenseMetaData;
import com.web.pocketbuddy.exception.GroupApiExceptions;
import com.web.pocketbuddy.payload.FindExpenseByDates;
import com.web.pocketbuddy.payload.RegisterGroupExpense;
import com.web.pocketbuddy.service.GroupDetailsService;
import com.web.pocketbuddy.service.GroupExpenseService;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import com.web.pocketbuddy.utils.ConfigService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@AllArgsConstructor
public class GroupExpenseResponseService implements GroupExpenseService {

    private final GroupDetailsService groupDetailsService;
    private final GroupExpenseMasterDao groupExpenseMasterDao;
    private final UserService userService;


    @Override
    public GroupExpenseDto addExpense(RegisterGroupExpense expensePayload) {
        GroupDocument savedGroup = groupDetailsService.findGroupDocumentById(expensePayload.getGroupId());
        GroupExpenseDocument request = MapperUtils.convertToGroupExpenseDocument(expensePayload);

        Map<String, Double> includingMembersMap = expensePayload.getIncludedMembers();
        Map<String, GroupExpenseMetaData> map = new HashMap<>();
        includingMembersMap.forEach((userId, amount) -> {
            UserDocument userDocument = userService.findUserById(userId);
            String fullname = userDocument.getUserFirstName() +" "+userDocument.getUserLastName();
            map.put(userId, new GroupExpenseMetaData(fullname, amount));
        });
        request.setIncludedMembers(map);

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
    public GroupExpenseDto updateExpense(GroupExpenseDto expenseDto) {
        GroupExpenseDocument savedExpense = findByExpenseId(expenseDto.getExpenseId());

        savedExpense.setExpenseDescription(expenseDto.getDescription());
        savedExpense.setExpenseAmount(expenseDto.getAmount());
        savedExpense.setIncludedMembers(expenseDto.getIncludedMembers());

        return MapperUtils.convertToGroupExpenseDto(groupExpenseMasterDao.save(savedExpense));
    }

    private GroupExpenseDocument findByExpenseId(String expenseId) {
        return groupExpenseMasterDao.findByExpenseId(expenseId)
                .orElseThrow(() -> new GroupApiExceptions("No Expense Found!", HttpStatus.NOT_FOUND));
    }

    @Override
    public String MarkExpenseAsDeleted(String expenseId) {
        GroupExpenseDocument savedExpense = groupExpenseMasterDao.findByExpenseId(expenseId)
                .orElseThrow(() -> new GroupApiExceptions("Expense Not Found!", HttpStatus.NOT_FOUND));
        savedExpense.setDeleted(true);
        groupExpenseMasterDao.save(savedExpense);
        return "Expense has been deleted";
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


    @Override
    public List<GroupExpenseDto> fetchAllExpenseByDates(FindExpenseByDates details) {
        if(org.springframework.util.StringUtils.isEmpty(details.getGroupId()) || org.springframework.util.StringUtils.isEmpty(details.getUserId())) {
            throw new GroupApiExceptions("Group id is empty!", HttpStatus.BAD_REQUEST);
        }

        List<GroupExpenseDocument> savedExpenses = fetchAllExpensesByGroup(details.getGroupId());
        if(ObjectUtils.isEmpty(savedExpenses)) {
            return Collections.emptyList();
        }

        return savedExpenses.stream()
                .filter(groupExpense ->
                       groupExpense.getCreatedDate().after(details.getStartDate())
                                && groupExpense.getCreatedDate().before(details.getStartDate()))
                .map(MapperUtils::convertToGroupExpenseDto)
                .toList();
    }

    private List<GroupExpenseDocument> fetchAllExpensesByGroupAndUser(String groupId, String userId) {

        List<GroupExpenseDocument> savedExpenses = groupExpenseMasterDao.findByGroupId(groupId);
        if(ObjectUtils.isEmpty(savedExpenses)) {
            return Collections.emptyList();
        }

        return savedExpenses.stream()
                .filter(expense -> !expense.isDeleted())
                .filter(expense -> userId.equals(expense.getRegisterByUserId()))
                .toList();
    }

    private List<GroupExpenseDocument> fetchAllExpensesByGroup(String groupId) {

        List<GroupExpenseDocument> savedExpenses = groupExpenseMasterDao.findByGroupId(groupId);
        if(ObjectUtils.isEmpty(savedExpenses)) {
            return Collections.emptyList();
        }

        return savedExpenses.stream()
                .filter(expense -> !expense.isDeleted())
                .toList();
    }

}
