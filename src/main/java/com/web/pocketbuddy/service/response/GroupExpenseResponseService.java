package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.entity.dao.GroupDetailsMasterDoa;
import com.web.pocketbuddy.entity.dao.GroupExpenseMasterDoa;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.exception.GroupApiExceptions;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.service.GroupExpenseService;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GroupExpenseResponseService implements GroupExpenseService {

    private final UserService userService;
    private final GroupDetailsMasterDoa groupDetailsMasterDoa;

    @Override
    public GroupDetailsResponse registerGroup(GroupRegisterDetails registerDetails) {
        GroupDocument groupDocument = MapperUtils.convertToGroupExpenseDocument(registerDetails);
        Map<String, String> joinedMembersMap = new HashMap<>();
        joinedMembersMap.put(registerDetails.getCreatedByUser(), fetchUserFullName(registerDetails.getCreatedByUser()));
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
    public GroupDetailsResponse deleteGroup(String groupId) {
        return null;
    }

    @Override
    public GroupDetailsResponse joinGroup(String groupId, String userId) {
        GroupDocument savedGroup = fetchGroupDocument(groupId);

        if(savedGroup.getMembers().containsKey(userId)) {
            throw new GroupApiExceptions("You already become a members of that Group", HttpStatus.BAD_REQUEST);
        }

        Map<String, String> joinedMembersMap = savedGroup.getMembers();
        if(ObjectUtils.isEmpty(joinedMembersMap)) {
            joinedMembersMap = new HashMap<>();
        }
        joinedMembersMap.put(userId, fetchUserFullName(userId));
        savedGroup.setMembers(joinedMembersMap);

        return MapperUtils.convertGroupDetailResponse(groupDetailsMasterDoa.save(savedGroup));
    }

    @Override
    public List<GroupDetailsResponse> getAllGroups(String userId) {
        return List.of();
    }

    private String fetchUserFullName(String userId) {
        UserDetailResponse savedUser = userService.findUserById(userId);
        return savedUser.getUserFirstName() + " " + savedUser.getUserLastName();
    }

    public GroupDocument fetchGroupDocument(String groupId) {
        return groupDetailsMasterDoa.findById(groupId)
                .orElseThrow(() -> new GroupApiExceptions("No Such Group Found!", HttpStatus.NOT_FOUND));
    }

}
