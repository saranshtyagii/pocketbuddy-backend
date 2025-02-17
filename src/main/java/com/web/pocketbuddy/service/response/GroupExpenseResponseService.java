package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.constants.ConstantsVariables;
import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.entity.dao.GroupDetailsMasterDoa;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.exception.GroupApiExceptions;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.service.GroupExpenseService;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@AllArgsConstructor
public class GroupExpenseResponseService implements GroupExpenseService {

    private final UserService userService;
    private final GroupDetailsMasterDoa groupDetailsMasterDoa;

    @Override
    public GroupDetailsResponse registerGroup(GroupRegisterDetails registerDetails) {
        GroupDocument groupDocument = MapperUtils.convertToGroupExpenseDocument(registerDetails);
        groupDocument.setDeleted(false);
        Map<String, String> joinedMembersMap = new HashMap<>();
        UserDocument savedUser = userService.findUserById(registerDetails.getCreatedByUser());
        String fullName = savedUser.getUserFirstName() + " " + savedUser.getUserLastName();
        joinedMembersMap.put(registerDetails.getCreatedByUser(), fullName);
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

        Map<String, String> joinedMembersMap = savedGroup.getMembers();
        if(ObjectUtils.isEmpty(joinedMembersMap)) {
            joinedMembersMap = new HashMap<>();
        }
        UserDocument savedUser = userService.findUserById(userId);
        String fullName = savedUser.getUserFirstName() + " " + savedUser.getUserLastName();

        joinedMembersMap.put(userId, fullName);
        savedGroup.setMembers(joinedMembersMap);

        List<GroupDocument> userJoinGroup = savedUser.getUserJoinGroup();
        if(userJoinGroup == null) {
            userJoinGroup = new ArrayList<>();
        }

        GroupDocument updateGroupDetails = groupDetailsMasterDoa.save(savedGroup);
        userJoinGroup.add(updateGroupDetails);

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
                    }
                });

        return "All the groups have been deleted which marked as deleted before Date: "+currentDate.toString();
    }

    public GroupDocument fetchGroupDocument(String groupId) {
        return groupDetailsMasterDoa.findById(groupId)
                .orElseThrow(() -> new GroupApiExceptions("No Such Group Found!", HttpStatus.NOT_FOUND));
    }

}
