package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.constants.ConstantsVariables;
import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.entity.dao.GroupDetailsMasterDoa;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.exception.AuthenticationException;
import com.web.pocketbuddy.exception.GroupApiExceptions;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.service.GroupExpenseService;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupExpenseResponseService implements GroupExpenseService {

    private final GroupDetailsMasterDoa groupExpenseMasterDoa;
    private final UserService userService;

    @Override
    public GroupDetailsResponse createGroup(GroupRegisterDetails request) {
        GroupDocument groupDetailsResponse = MapperUtils.convertToGroupDocument(request);
        Map<String, Double> joinMembers = new HashMap<>();
        joinMembers.put(request.getCreatedByUser(), 0.00);

        UserDocument savedUser = userService.findUserById(request.getCreatedByUser());
        List<String> joinedGroups = savedUser.getUserJoinGroupId();
        if(CollectionUtils.isEmpty(joinedGroups)) {
            joinedGroups = new ArrayList<>();
            joinedGroups.add(request.getCreatedByUser());
            savedUser.setUserJoinGroupId(joinedGroups);
            userService.saveOrUpdate(savedUser);
        }

        groupDetailsResponse.setJoinedMembersWithExpenseAmount(joinMembers);
        return MapperUtils.convertGroupDetailResponse(groupExpenseMasterDoa.save(groupDetailsResponse));
    }

    @Override
    public List<GroupDetailsResponse> findAllGroups(String userId) {

        UserDocument savedUser = userService.findUserById(userId);

        List<String> joinedGroups = savedUser.getUserJoinGroupId();
        if(CollectionUtils.isEmpty(joinedGroups)) {
            return new ArrayList<>();
        }

        List<GroupDetailsResponse> groupsList = new ArrayList<>();

        joinedGroups.stream().map(groupId -> {
            GroupDocument groupDocument = findGroupAsGroupDocumentById(groupId);
            if(!groupDocument.isDeleted()) {
                groupsList.add(MapperUtils.convertGroupDetailResponse(groupDocument));
            }
            return null;
        });

        return groupsList;
    }

    @Override
    public GroupDetailsResponse findGroupById(String groupId) {
        return MapperUtils.convertGroupDetailResponse(findGroupAsGroupDocumentById(groupId));
    }

    @Override
    public List<GroupDetailsResponse> findGroupByName(String groupName) {
        List<GroupDocument> savedGroups = groupExpenseMasterDoa.findAllByGroupName(groupName)
                .orElseThrow(() -> new GroupApiExceptions("No Such Group Found with name: "+groupName, HttpStatus.NOT_FOUND));

        return savedGroups.stream()
                .filter(groupDocument -> !groupDocument.isDeleted())
                .map(MapperUtils::convertGroupDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public String deleteGroup(String userId, String groupId) {
        GroupDocument savedGroup = findGroupAsGroupDocumentById(groupId);
        if(!savedGroup.getCreatedByUser().equals(userId)) {
            throw new GroupApiExceptions("You don't have a delete right", HttpStatus.FORBIDDEN);
        }
        savedGroup.setDeleted(true);
        groupExpenseMasterDoa.save(savedGroup);
        return "Group successfully deleted";
    }

    @Override
    public GroupDetailsResponse updateGroup(String groupId) {
        return null;
    }

    @Override
    public GroupDetailsResponse joinGroup(String userId, String groupId) {
        UserDocument savedUser = userService.findUserById(userId);
        GroupDocument savedGroup = findGroupAsGroupDocumentById(groupId);

        Map<String, Double> joinMembers = savedGroup.getJoinedMembersWithExpenseAmount();
        if(CollectionUtils.isEmpty(joinMembers)) {
            joinMembers = new HashMap<>();
            joinMembers.put(savedUser.getUserId(), 0.00);
            savedGroup.setJoinedMembersWithExpenseAmount(joinMembers);
        }

        List<String> joinList = savedUser.getUserJoinGroupId();
        if(CollectionUtils.isEmpty(joinList)) {
            joinList = new ArrayList<>();
            joinList.add(groupId);
            savedUser.setUserJoinGroupId(joinList);
            userService.saveOrUpdate(savedUser);
        }

        return MapperUtils.convertGroupDetailResponse(groupExpenseMasterDoa.save(savedGroup));
    }

    @Override
    public String leaveGroup(String userId, String groupId) {

        UserDocument savedUser = userService.findUserById(userId);
        List<String> joinedGroups = savedUser.getUserJoinGroupId();
        if(!CollectionUtils.isEmpty(joinedGroups)) {
            joinedGroups.remove(groupId);
        }

        return "Group successfully Leaved";
    }

    @Override
    public void deleteGroupFromDB(String ApiKey, String groupId, String userId) {
        if(!ConstantsVariables.API_KEY.equals(ApiKey)) {
            throw new AuthenticationException("Invalid Api Key", HttpStatus.UNAUTHORIZED);
        }

        if(!StringUtils.isEmpty(groupId)) {
            if(!StringUtils.isEmpty(userId)) {
                leaveGroup(userId, groupId);
            }
            groupExpenseMasterDoa.deleteById(groupId);
            return;
        }

        List<GroupDocument> allGroups = groupExpenseMasterDoa.findAll();
        if(CollectionUtils.isEmpty(allGroups)) {
            return;
        }

        allGroups.parallelStream().forEach(group -> {
            if(group.isDeleted()) {
                Map<String, Double> joinMembers = group.getJoinedMembersWithExpenseAmount();
                if(!CollectionUtils.isEmpty(joinMembers)) {
                    for(Map.Entry<String, Double> entry : joinMembers.entrySet()) {
                        leaveGroup(entry.getKey(), group.getGroupId());
                    }
                }
                groupExpenseMasterDoa.delete(group);
            }
        });
    }

    @Override
    public GroupDetailsResponse recoverDeletedGroup(String userId, String groupId) {
        GroupDocument savedGroup;
        try {
            savedGroup = findGroupAsGroupDocumentById(groupId);
        } catch (Exception e) {
            throw new GroupApiExceptions("Sorry we can't able to recover your group!", HttpStatus.BAD_REQUEST);
        }

        if(!userId.equals(savedGroup.getCreatedByUser())) {
            throw new GroupApiExceptions("You are not group admin", HttpStatus.FORBIDDEN);
        }

        savedGroup.setDeleted(false);
        return MapperUtils.convertGroupDetailResponse(savedGroup);
    }


    private GroupDocument findGroupAsGroupDocumentById(String groupId) {
        return groupExpenseMasterDoa.findById(groupId)
                .orElseThrow(() ->
                        new GroupApiExceptions("No Such Group Found", HttpStatus.BAD_REQUEST));
    }
}
