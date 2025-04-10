package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.constants.ConstantsVariables;
import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.dto.GroupDetailsDto;
import com.web.pocketbuddy.entity.dao.GroupDetailsMasterDoa;
import com.web.pocketbuddy.entity.dao.GroupExpenseMasterDoa;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.GroupExpenseDocument;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.exception.GroupApiExceptions;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.service.GroupDetailsService;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@AllArgsConstructor
public class GroupDetailsResponseService implements GroupDetailsService {

    private final UserService userService;
    private final GroupDetailsMasterDoa groupDetailsMasterDoa;
    private final GroupExpenseMasterDoa groupExpenseMasterDoa;

    @Override
    public GroupDetailsResponse registerGroup(GroupRegisterDetails registerDetails) {
        GroupDocument groupDocument = MapperUtils.convertToGroupDocument(registerDetails);
        groupDocument.setDeleted(false);

        Map<String, Map<String, Double>> joinedMembersMap = new HashMap<>();
        UserDocument savedUser = userService.findUserById(registerDetails.getCreatedByUser());
        String fullName = savedUser.getUserFirstName() + " " + savedUser.getUserLastName();
        Map<String, Double> membersMap = new HashMap<>();
        membersMap.put(fullName, 0.0);
        joinedMembersMap.put(registerDetails.getCreatedByUser(), membersMap);
        groupDocument.setMembers(joinedMembersMap);

        GroupDocument savedGroupDocument = groupDetailsMasterDoa.save(groupDocument);

        return MapperUtils.convertGroupDetailResponse(savedGroupDocument);
    }

    @Override
    public GroupDetailsResponse getGroupDetails(String groupId) {
        return null;
    }

    @Override
    public GroupDetailsResponse updateGroupDetails(String groupId, GroupDetailsResponse groupDetailsResponse) {
        return null;
    }

    @Override
    public String deleteGroup(String groupId, String userId) {
        GroupDocument savedGroup = fetchGroupDocument(groupId);
        if(!userId.equals(savedGroup.getCreatedByUser())) {
            throw new GroupApiExceptions("You don't have a right to delete this Group.", HttpStatus.FORBIDDEN);
        }
        savedGroup.setDeleted(true);
        savedGroup.setGroupDeletedDate(new Date());
        groupDetailsMasterDoa.save(savedGroup);
        return "Group " + groupId + " has been deleted";
    }

    @Override
    public GroupDetailsResponse joinGroup(String groupId, String userId) {
        GroupDocument savedGroup = fetchGroupDocument(groupId);

        if(savedGroup.getMembers().containsKey(userId)) {
            throw new GroupApiExceptions("You already become a members of that Group", HttpStatus.BAD_REQUEST);
        }

        Map<String, Map<String, Double>> joinedMembersMap = savedGroup.getMembers();
        if(ObjectUtils.isEmpty(joinedMembersMap)) {
            joinedMembersMap = new HashMap<>();
        }
        UserDocument savedUser = userService.findUserById(userId);
        String fullName = savedUser.getUserFirstName() + " " + savedUser.getUserLastName();

        Map<String, Double> membersMap = new HashMap<>();
        membersMap.put(fullName, 0.0);
        joinedMembersMap.put(userId,  membersMap);
        savedGroup.setMembers(joinedMembersMap);

        List<GroupDocument> userJoinGroup = savedUser.getUserJoinGroup();
        if(userJoinGroup == null) {
            userJoinGroup = new ArrayList<>();
        }
        GroupDocument updateGroupDetails = groupDetailsMasterDoa.save(savedGroup);
        userJoinGroup.add(updateGroupDetails);

        userService.savedUpdatedUser(savedUser);

        return MapperUtils.convertGroupDetailResponse(updateGroupDetails);
    }

    @Override
    public List<GroupDetailsResponse> getAllGroups(String userId) {
        return List.of();
    }

    @Override
    public GroupDetailsResponse findGroupById(String groupId) {
        GroupDocument groupDocument = fetchGroupDocument(groupId);
        return MapperUtils.convertGroupDetailResponse(groupDocument);
    }

    @Override
    public String deleteGroupFromDb(String apiKey) {
        if(!apiKey.equals(ConstantsVariables.API_KEY)) {
            throw new GroupApiExceptions("Its not that easy :D", HttpStatus.FORBIDDEN);
        }
        List<GroupDocument> savedGroups = groupDetailsMasterDoa.findAll();

        if(CollectionUtils.isEmpty(savedGroups)) {
            return "There is no group to delete.";
        }

        // delete isDeleted Groups having date is greater than 30 days,
        Date currentDate = new Date();
        savedGroups.stream()
                .filter(GroupDocument::isDeleted)
                .forEach(groupDocument -> {
                    if(groupDocument.getGroupDeletedDate().before(currentDate)) {
                        groupDetailsMasterDoa.delete(groupDocument);
                        groupExpenseMasterDoa.deleteByGroupId(groupDocument.getGroupId());
                    }
                });

        return "All the groups have been deleted which marked as deleted before Date: "+currentDate.toString();
    }

    @Override
    public List<GroupDetailsDto> findGroupExpensesByGroupId(String groupId) {
        List<GroupExpenseDocument> savedExpenses = findNonDeletedGroupExpenses(groupId);
        if(CollectionUtils.isEmpty(savedExpenses)) {
            return new ArrayList<>();
        }
        return savedExpenses.stream().filter(expense -> !expense.getIsDeleted())
                .map(MapperUtils::toGroupDetailsDto)
                .toList();
    }

    @Override
    public String markExpenseAsDeleted(String expenseId, String userId) {
        GroupDocument savedGroup = fetchGroupDocument(expenseId);
        if(!userId.equals(savedGroup.getCreatedByUser())) {
            throw new GroupApiExceptions("You don't have a right to delete this Group.", HttpStatus.FORBIDDEN);
        }
        savedGroup.setDeleted(true);
        savedGroup.setGroupDeletedDate(new Date());
        savedGroup.setLastUpdateDate(new Date());

        groupDetailsMasterDoa.save(savedGroup);
        return "Expense " + expenseId + " has been deleted";
    }

    @Override
    public List<GroupDetailsDto> addExpense(GroupDetailsDto groupDetailsDto) {
        GroupExpenseDocument expenseDocument = MapperUtils.convertToGroupDetailsDocument(groupDetailsDto);
        return findNonDeletedGroupExpenses(expenseDocument.getGroupId()).stream().map(MapperUtils::toGroupDetailsDto).toList();
    }

    @Override
    public GroupDocument findByGroupIdAsDoc(String groupId) {
        return fetchGroupDocument(groupId);
    }

    private GroupDocument fetchGroupDocument(String groupId) {
        return groupDetailsMasterDoa.findById(groupId)
                .orElseThrow(() -> new GroupApiExceptions("No Such Group Found!", HttpStatus.NOT_FOUND));
    }

    private GroupExpenseDocument findGroupExpenseById(String expenseId) {
        return groupExpenseMasterDoa.findById(expenseId)
                .orElseThrow(() -> new GroupApiExceptions("No Such Expense Found!", HttpStatus.NOT_FOUND));
    }

    private List<GroupExpenseDocument> findNonDeletedGroupExpenses(String groupId) {
        List<GroupExpenseDocument> saved = groupExpenseMasterDoa.findByGroupId(groupId)
                .orElse(null);
        if(CollectionUtils.isEmpty(saved)) {
            return saved;
        }
        return saved.stream().filter(expense -> !expense.getIsDeleted()).toList();
    }

}
