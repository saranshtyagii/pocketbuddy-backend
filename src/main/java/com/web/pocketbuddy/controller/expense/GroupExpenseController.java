package com.web.pocketbuddy.controller.expense;

import com.web.pocketbuddy.constants.UrlsConstants;
import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.dto.GroupExpensesDto;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.service.GroupExpenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlsConstants.GROUP_URL)
@AllArgsConstructor
public class GroupExpenseController {

    private final GroupExpenseService groupExpenseService;

    // Group Related Service

    @PostMapping("/new-group")
    public ResponseEntity<GroupDetailsResponse> registerGroup(@RequestBody GroupRegisterDetails registerDetails) {
        return new ResponseEntity<>(groupExpenseService.registerGroup(registerDetails), HttpStatus.CREATED);
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
        return ResponseEntity.ok(groupExpenseService.deleteGroupFromDb(apiKey));
    }

    // Group Expense Related Service

    @PostMapping("/add-expense")
    public ResponseEntity<List<GroupExpensesDto>> addExpense(@RequestBody GroupExpensesDto groupExpensesDto) {
        return new ResponseEntity<>(groupExpenseService.addExpense(groupExpensesDto), HttpStatus.CREATED);
    }

    @GetMapping("/find-expenses")
    public ResponseEntity<List<GroupExpensesDto>> getAllGroupExpenses(@RequestParam String groupId) {
        return ResponseEntity.ok(groupExpenseService.findGroupExpensesByGroupId(groupId));
    }

    @DeleteMapping("/remove-expense")
    public ResponseEntity<String> removeExpense(@RequestParam String expenseId, @RequestParam String userId) {
        return ResponseEntity.ok(groupExpenseService.markExpenseAsDeleted(expenseId, userId));
    }

}
