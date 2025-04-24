package com.web.pocketbuddy.controller.expense;

import com.web.pocketbuddy.constants.UrlsConstants;
import com.web.pocketbuddy.dto.PersonalExpenseResponse;
import com.web.pocketbuddy.payload.AddPersonalExpense;
import com.web.pocketbuddy.payload.FindExpenseByDates;
import com.web.pocketbuddy.payload.RegisterPersonalExpense;
import com.web.pocketbuddy.service.PersonalExpenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlsConstants.PERSONAL_URL)
@AllArgsConstructor
public class PersonalExpenseController {

    private final PersonalExpenseService personalExpenseService;

    @PostMapping("/register")
    public ResponseEntity<PersonalExpenseResponse> registerExpense(@RequestBody AddPersonalExpense expense) {
        return ResponseEntity.ok(personalExpenseService.addPersonalExpense(expense));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateExpense(@RequestBody RegisterPersonalExpense expense) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteExpense(@RequestParam String expenseId) {
        return ResponseEntity.ok(personalExpenseService.markExpenseAsDeleted(expenseId));
    }

    @GetMapping("/findbydate")
    public ResponseEntity<Void> findExpense(@RequestBody FindExpenseByDates expense) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<PersonalExpenseResponse>> fetchAll(@RequestParam String userId) {
        return ResponseEntity.ok(personalExpenseService.fetchAllPersonalExpensesByUserId(userId));
    }

    @PostMapping("/fetch-by-dates")
    public ResponseEntity<List<PersonalExpenseResponse>> fetchByDates(@RequestBody FindExpenseByDates details) {
        return ResponseEntity.ok(personalExpenseService.fetchAllExpenseByDates(details));
    }

}
