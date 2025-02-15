package com.web.pocketbuddy.controller.expense;

import com.web.pocketbuddy.constants.ConstantsUrls;
import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.service.GroupExpenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ConstantsUrls.GROUP_URL)
@AllArgsConstructor
public class GroupExpense {

    private final GroupExpenseService groupExpenseService;

    @PostMapping("/new-group")
    public ResponseEntity<GroupDetailsResponse> registerGroup(@RequestBody GroupRegisterDetails registerDetails) {
        return new ResponseEntity<>(groupExpenseService.registerGroup(registerDetails), HttpStatus.CREATED);
    }

    @PatchMapping("/join-group")
    public ResponseEntity<GroupDetailsResponse> joinGroup(@RequestParam String groupId, @RequestParam String userId) {
        return ResponseEntity.ok(groupExpenseService.joinGroup(groupId, userId));
    }

}
