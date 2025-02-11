package com.web.pocketbuddy.controller.expense;

import com.web.pocketbuddy.constants.ConstantsUrls;
import com.web.pocketbuddy.payload.FindExpenseByDates;
import com.web.pocketbuddy.payload.RegisterPersonalExpense;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(ConstantsUrls.PERSONAL_URL)
public class PersonalExpense {

    @GetMapping("/register")
    public ResponseEntity<Void> registerExpense(@RequestBody RegisterPersonalExpense expense) {
        return new ResponseEntity<>(HttpStatus.CREATED);
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
