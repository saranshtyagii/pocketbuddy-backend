package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.entity.dao.GroupDetailsMasterDao;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.exception.GroupApiExceptions;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.service.GroupDetailsService;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import com.web.pocketbuddy.utils.ConfigService;
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
    private final GroupDetailsMasterDao groupDetailsMasterDoa;

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
        Map<String, Double> members = new HashMap<>();
        members.put(registerDetails.getCreatedByUser(), 0.0);
        createGroupDocument.setMembers(members);
        // create Group
        GroupDocument savedGroup = groupDetailsMasterDoa.save(createGroupDocument);

        // update user joined group
        joinedGroupIds.add(savedGroup.getGroupId());
        savedUser.setUserJoinGroupId(joinedGroupIds);
        userService.savedUpdatedUser(savedUser);

        return MapperUtils.convertGroupDetailResponse(savedGroup);

    }

    @Override
    public GroupDetailsResponse updateGroupDetails(GroupDetailsResponse groupDetailsResponse) {
        GroupDocument savedGroup = fetchGroupById(groupDetailsResponse.getGroupId());

        savedGroup.setGroupName(groupDetailsResponse.getGroupName());
        savedGroup.setDescription(groupDetailsResponse.getGroupDescription());

        return MapperUtils.convertGroupDetailResponse(groupDetailsMasterDoa.save(savedGroup));
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
    public void deleteFromDb(String groupId, String apiKey) {
        if(!apiKey.equals(ConfigService.getConfig().getApiKey())) {
            throw new GroupApiExceptions("Invalid Api Key", HttpStatus.FORBIDDEN);
        }

        if(org.apache.commons.lang3.StringUtils.isNotBlank(groupId)) {
            groupDetailsMasterDoa.deleteById(groupId);
            return;
        }

        List<GroupDocument> savedGroup = groupDetailsMasterDoa.findAll();

        savedGroup.parallelStream().forEach(group -> {
            if(group.isDeleted()) {
                groupDetailsMasterDoa.delete(group);
            }
        });
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

        if(StringUtils.isEmpty(groupId) && !apiKey.equals(ConfigService.getConfig().getApiKey())) {
            throw new GroupApiExceptions("Its not that easy my friend :-)", HttpStatus.FORBIDDEN);
        }

        if(!StringUtils.isEmpty(groupId)) {
            groupDetailsMasterDoa.delete(fetchGroupById(groupId));
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

    @Override
    public GroupDocument findGroupDocumentById(String groupId) {
        return groupDetailsMasterDoa.findById(groupId).orElseThrow(() -> new GroupApiExceptions("Group not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public void saveOrUpdate(GroupDocument savedGroup) {
        groupDetailsMasterDoa.save(savedGroup);
    }


    private GroupDocument fetchGroupById(String groupId) {
        return groupDetailsMasterDoa.findByGroupId(groupId)
                .orElseThrow(() -> new GroupApiExceptions("No such group found!", HttpStatus.NOT_FOUND));
    }

}
