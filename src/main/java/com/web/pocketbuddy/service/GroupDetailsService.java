package com.web.pocketbuddy.service;

import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.dto.GroupDetailsDto;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.payload.GroupRegisterDetails;

import java.util.List;

public interface GroupDetailsService {

    public GroupDetailsResponse registerGroup(GroupRegisterDetails registerDetails);
    public GroupDetailsResponse updateGroupDetails(GroupDetailsResponse groupDetailsResponse);
    public String deleteGroup(String groupId, String userId);
    public GroupDetailsResponse joinGroup(String groupId, String userId);
    public void deleteFromDb(String groupId, String apiKey);
    public List<GroupDetailsResponse> getAllGroups(String userId);

    public GroupDetailsResponse findGroupById(String groupId);

    public String deleteGroupFromDb(String apiKey, String groupId);

    public GroupDocument findGroupDocumentById(String groupId);

    void saveOrUpdate(GroupDocument savedGroup);
}
