package com.web.pocketbuddy.service;

import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.payload.GroupRegisterDetails;

import java.util.List;
import java.util.Map;

public interface GroupDetailsService {

    GroupDetailsResponse registerGroup(GroupRegisterDetails registerDetails);
    GroupDetailsResponse updateGroupDetails(GroupDetailsResponse groupDetailsResponse);
    String deleteGroup(String groupId, String userId);
    GroupDetailsResponse joinGroup(String groupId, String userId);
    void deleteFromDb(String groupId, String apiKey);
    List<GroupDetailsResponse> getAllGroups(String userId);
    GroupDetailsResponse findGroupById(String groupId);
    GroupDetailsResponse findGroupByDiscoverableId(String discoverableId);
    String deleteGroupFromDb(String apiKey, String groupId);
    GroupDocument findGroupDocumentById(String groupId);
    void saveOrUpdate(GroupDocument savedGroup);
    Map<String, String> fetchGroupJoinMembers(String groupId);
}
