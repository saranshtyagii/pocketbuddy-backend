package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.constants.ConstantsVariables;
import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.entity.dao.GroupDetailsMasterDoa;
import com.web.pocketbuddy.entity.dao.GroupExpenseMasterDoa;
import com.web.pocketbuddy.entity.document.GroupDocument;
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
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class GroupDetailsResponseService implements GroupDetailsService {

    private final UserService userService;
    private final GroupDetailsMasterDoa groupDetailsMasterDoa;
    private final GroupExpenseMasterDoa groupExpenseMasterDoa;


    @Override
    public GroupDetailsResponse registerGroup(GroupRegisterDetails registerDetails) {
        GroupDocument createGroupDocument = MapperUtils.convertToGroupDocument(registerDetails);

        // fetch Saved User details
        UserDocument savedUser = userService.findUserById(registerDetails.getCreatedByUser());

        // checkout for joined groupDetails
        List<String> joinedGroupIds = savedUser.getUserJoinGroupId();
        if(CollectionUtils.isEmpty(joinedGroupIds)) {
            joinedGroupIds = new ArrayList<>();
        }

        // create Group
        GroupDocument savedGroup = groupDetailsMasterDoa.save(createGroupDocument);

        // update user joined group
        joinedGroupIds.add(savedGroup.getGroupId());
        savedUser.setUserJoinGroupId(joinedGroupIds);
        userService.savedUpdatedUser(savedUser);

        return MapperUtils.convertGroupDetailResponse(savedGroup);

    }

    @Override
    public GroupDetailsResponse getGroupDetails(String groupId) {
        return MapperUtils.convertGroupDetailResponse(fetchGroupById(groupId));
    }

    @Override
    public GroupDetailsResponse updateGroupDetails(String groupId, GroupDetailsResponse groupDetailsResponse) {
        return null;
    }

    @Override
    public String deleteGroup(String groupId, String userId) {
        GroupDocument savedGroup = fetchGroupById(groupId);

        if(!savedGroup.getCreatedByUser().equals(userId)) {
            throw new GroupApiExceptions("You are not group admin", HttpStatus.FORBIDDEN);
        }

        savedGroup.setDeleted(true);
        groupDetailsMasterDoa.save(savedGroup);
        return "Group " + savedGroup.getGroupName() + " has been deleted";
    }

    @Override
    public GroupDetailsResponse joinGroup(String groupId, String userId) {
        // fetch saved Group
        GroupDocument savedGroup = fetchGroupById(groupId);

        // fetch saved user
        UserDocument savedUser = userService.findUserById(userId);

        // check for join groups
        List<String> joinedGroupIds = savedUser.getUserJoinGroupId();
        if(CollectionUtils.isEmpty(joinedGroupIds)) {
            joinedGroupIds = new ArrayList<>();
        }

        joinedGroupIds.add(savedGroup.getGroupId());
        savedUser.setUserJoinGroupId(joinedGroupIds);
        userService.savedUpdatedUser(savedUser);

        // add user as group members
        Map<String, Double> members = savedGroup.getMembers();
        if(CollectionUtils.isEmpty(members)) {
            members = new HashMap<>();
        }
        members.put(savedUser.getUserId(), 0.0);
        savedGroup.setMembers(members);
        GroupDocument updatedGroup = groupDetailsMasterDoa.save(savedGroup);

        GroupDetailsResponse groupDetailsResponse = new GroupDetailsResponse();

        // change userId with username name
        Map<String, Double> groupMembers = new HashMap<>();
        updatedGroup.getMembers().forEach((id, amount) -> {
            UserDocument userDocument = userService.findUserById(id);
            groupMembers.put(userDocument.getUserFirstName(), amount);
        });
        groupDetailsResponse.setJoinedMembers(groupMembers);
        return groupDetailsResponse;
    }

    @Override
    public List<GroupDetailsResponse> getAllGroups(String userId) {
        UserDocument userDocument = userService.findUserById(userId);

        // fetch joined Group Document id
        List<String> joinedGroupIds = userDocument.getUserJoinGroupId();
        if(CollectionUtils.isEmpty(joinedGroupIds)) {
            return Collections.emptyList();
        }

        List<GroupDetailsResponse> joinedGroups = new ArrayList<>();
        joinedGroupIds.forEach(groupId-> {
            GroupDocument savedGroup = fetchGroupById(groupId);
            if(!savedGroup.isDeleted()) {
                joinedGroups.add(MapperUtils.convertGroupDetailResponse(savedGroup));
            }
        });

        return joinedGroups;
    }

    @Override
    public GroupDetailsResponse findGroupById(String groupId) {
        return MapperUtils.convertGroupDetailResponse(fetchGroupById(groupId));
    }

    @Override
    public String deleteGroupFromDb(String apiKey, String groupId) {

        if(StringUtils.isEmpty(groupId) && !apiKey.equals(ConstantsVariables.API_KEY)) {
            throw new GroupApiExceptions("Its not that easy my friend :-)", HttpStatus.FORBIDDEN);
        }

        if(!StringUtils.isEmpty(groupId)) {
            groupDetailsMasterDoa.deleteById(groupId);
            return "Group " + groupId + " has been deleted";
        }

        List<GroupDocument> allGroups = groupDetailsMasterDoa.findAll();
        AtomicInteger count = new AtomicInteger(1);
        allGroups.forEach(group -> {
            if(group.isDeleted()) {
                groupDetailsMasterDoa.deleteById(groupId);
                count.addAndGet(1);
            }
        });
        return count + " groups have been deleted";
    }


    private GroupDocument fetchGroupById(String groupId) {
        return groupDetailsMasterDoa.findById(groupId)
                .orElseThrow(() -> new GroupApiExceptions("No such group found!", HttpStatus.NOT_FOUND));
    }

}
