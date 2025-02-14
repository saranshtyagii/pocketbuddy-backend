package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.service.GroupExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupExpenseResponseService implements GroupExpenseService {
    @Override
    public GroupDetailsResponse registerGroup(GroupRegisterDetails registerDetails) {
        return null;
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
    public List<GroupDetailsResponse> getAllGroups(String userId) {
        return List.of();
    }
}
