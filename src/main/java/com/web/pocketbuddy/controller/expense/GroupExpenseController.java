package com.web.pocketbuddy.controller.expense;

import com.web.pocketbuddy.constants.UrlsConstants;
import com.web.pocketbuddy.dto.GroupExpenseDto;
import com.web.pocketbuddy.payload.RegisterGroupExpense;
import com.web.pocketbuddy.service.GroupExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlsConstants.GROUP_EXPENSE_URL)
public class GroupExpenseController {

    private final GroupExpenseService groupExpenseService;

    public GroupExpenseController(GroupExpenseService groupExpenseService) {
        this.groupExpenseService = groupExpenseService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<GroupExpenseDto>> getAllGroupExpenses(@RequestParam String groupId) {
        return ResponseEntity.ok(groupExpenseService.fetchGroupExpenseByGroupId(groupId));
    }

    @PostMapping("/register")
    public ResponseEntity<GroupExpenseDto> registerGroupExpense(@RequestBody RegisterGroupExpense request) {
        try {
            GroupExpenseDto result = groupExpenseService.addExpense(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }
}
