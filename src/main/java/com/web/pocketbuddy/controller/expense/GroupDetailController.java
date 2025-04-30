package com.web.pocketbuddy.controller.expense;

import com.web.pocketbuddy.constants.UrlsConstants;
import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.service.GroupDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlsConstants.GROUP_URL)
@AllArgsConstructor
public class GroupDetailController {

    private final GroupDetailsService groupExpenseService;

    // Group Related Service

    @PostMapping("/new-group")
    public ResponseEntity<GroupDetailsResponse> registerGroup(@RequestBody GroupRegisterDetails registerDetails) {
        return new ResponseEntity<>(groupExpenseService.registerGroup(registerDetails), HttpStatus.OK);
    }

    @PatchMapping("/join-group")
    public ResponseEntity<GroupDetailsResponse> joinGroup(@RequestParam String groupId, @RequestParam String userId) {
        return ResponseEntity.ok(groupExpenseService.joinGroup(groupId, userId));
    }

    @GetMapping("/find")
    public ResponseEntity<GroupDetailsResponse> findGroup(@RequestParam String groupId) {
        return ResponseEntity.ok(groupExpenseService.findGroupById(groupId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteGroup(@RequestParam String groupId, @RequestParam String userId) {
        return ResponseEntity.ok(groupExpenseService.deleteGroup(groupId, userId));
    }

    @GetMapping("/remove-groups")
    public ResponseEntity<String> removeGroup(@RequestParam String apiKey) {
        return ResponseEntity.ok(groupExpenseService.deleteGroupFromDb(apiKey, null));
    }

    @GetMapping("/find-user-joined-groups")
    public ResponseEntity<List<GroupDetailsResponse>> findUserJoinedGroups(@RequestParam String userId) {
        return ResponseEntity.ok(groupExpenseService.getAllGroups(userId));
    }

}
