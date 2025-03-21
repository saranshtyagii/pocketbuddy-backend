package com.web.pocketbuddy.controller.expense;

import com.web.pocketbuddy.constants.ConstantsUrls;
import com.web.pocketbuddy.dto.PersonalExpenseResponse;
import com.web.pocketbuddy.payload.AddPersonalExpense;
import com.web.pocketbuddy.payload.FindExpenseByDates;
import com.web.pocketbuddy.service.PersonalExpenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ConstantsUrls.PERSONAL_URL)
@AllArgsConstructor
public class PersonalExpenseController {

    private final PersonalExpenseService personalExpenseService;

    @GetMapping("/register")
    public ResponseEntity<PersonalExpenseResponse> registerExpense(@RequestBody AddPersonalExpense expense) {
        return ResponseEntity.ok(personalExpenseService.addPersonalExpense(expense));
    }

    @PutMapping("/update")
    public ResponseEntity<PersonalExpenseResponse> updateExpense(@RequestBody AddPersonalExpense expense) {
        return ResponseEntity.ok(personalExpenseService.updatePersonalExpense(expense));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteExpense(@PathVariable String expenseId) {
        return ResponseEntity.ok(personalExpenseService.deletePersonalExpense(expenseId));
    }

    @GetMapping("/findbydate")
    public ResponseEntity<List<PersonalExpenseResponse>> findExpense(@RequestBody FindExpenseByDates datesData) {
        return ResponseEntity.ok(personalExpenseService.getPersonalExpensesInRange(datesData));
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<PersonalExpenseResponse>> fetchAllByUserId(@RequestParam String userId) {
        return ResponseEntity.ok(personalExpenseService.fetchAllPersonalExpensesByUserId(userId));
    }
}
