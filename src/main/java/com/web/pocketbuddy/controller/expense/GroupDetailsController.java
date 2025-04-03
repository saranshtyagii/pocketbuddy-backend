package com.web.pocketbuddy.controller.expense;

import com.web.pocketbuddy.constants.UrlsConstants;
import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.service.GroupExpenseService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(UrlsConstants.GROUP_URL)
public class GroupDetailsController {

    private final GroupExpenseService groupExpenseService;

    @GetMapping("/")
    public ResponseEntity<List<GroupDetailsResponse>> getGroupDetails(@RequestParam String userId) {
        return ResponseEntity.ok(groupExpenseService.findAllGroups(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<GroupDetailsResponse> createGroup(@RequestBody GroupRegisterDetails request) {
        return ResponseEntity.ok(groupExpenseService.createGroup(request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteGroup(@RequestParam String userId, @RequestParam String groupId) {
        return ResponseEntity.ok(groupExpenseService.deleteGroup(userId, groupId));
    }

    @GetMapping("/leave")
    public ResponseEntity<String> leaveGroup(@RequestParam String userId, @RequestParam String groupId) {
        return ResponseEntity.ok(groupExpenseService.leaveGroup(userId, groupId));
    }

    @GetMapping("/find")
    public ResponseEntity<GroupDetailsResponse> findGroupById(@PathVariable String groupId) {
        return ResponseEntity.ok(groupExpenseService.findGroupById(groupId));
    }

    @GetMapping("/findbyname")
    public ResponseEntity<List<GroupDetailsResponse>> findGroupByName(@RequestParam String groupName) {
        return ResponseEntity.ok(groupExpenseService.findGroupByName(groupName));
    }

    @GetMapping("/recover")
    public ResponseEntity<GroupDetailsResponse> recoverGroup(@RequestParam String userId, @RequestParam String groupId) {
        return ResponseEntity.ok(groupExpenseService.recoverDeletedGroup(userId, groupId));
    }

    @GetMapping("/join")
    public ResponseEntity<GroupDetailsResponse> joinGroup(@RequestParam String userId, @RequestParam String groupId) {
        return ResponseEntity.ok(groupExpenseService.joinGroup(userId, groupId));
    }

    @PostMapping("/update")
    public ResponseEntity<GroupDetailsResponse> updateGroup(@RequestBody GroupRegisterDetails request) {
        return ResponseEntity.ok(groupExpenseService.updateGroup(request.getGroupName()));
    }

}
