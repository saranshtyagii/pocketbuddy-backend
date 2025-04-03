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

@RestController
@RequestMapping(UrlsConstants.PERSONAL_URL)
@AllArgsConstructor
public class PersonalExpenseController {

    private final PersonalExpenseService personalExpenseService;

    @GetMapping("/register")
    public ResponseEntity<PersonalExpenseResponse> registerExpense(@RequestBody AddPersonalExpense expense) {
        return ResponseEntity.ok(personalExpenseService.addPersonalExpense(expense));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateExpense(@RequestBody RegisterPersonalExpense expense) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteExpense(@PathVariable String expenseId) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/findbydate")
    public ResponseEntity<Void> findExpense(@RequestBody FindExpenseByDates expense) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



}
