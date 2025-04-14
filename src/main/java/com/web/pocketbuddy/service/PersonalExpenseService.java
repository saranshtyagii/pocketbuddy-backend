package com.web.pocketbuddy.service;

import com.web.pocketbuddy.dto.PersonalExpenseResponse;
import com.web.pocketbuddy.payload.AddPersonalExpense;
import com.web.pocketbuddy.payload.FetchByDates;

import java.util.List;

public interface PersonalExpenseService {

    public PersonalExpenseResponse getPersonalExpense(String id);
    public PersonalExpenseResponse addPersonalExpense(AddPersonalExpense expense);
    public PersonalExpenseResponse updatePersonalExpense(AddPersonalExpense expense);

    public String markExpenseAsDeleted(String expenseId);
    public String deletePersonalExpenseFromDb(String apiKey, String expenseId);
    public List<PersonalExpenseResponse> getPersonalExpensesInRange(FetchByDates fetchByDates);

    public List<PersonalExpenseResponse> fetchAllPersonalExpenses();
    public List<PersonalExpenseResponse> fetchAllPersonalExpensesByUserId(String userId);

    public double getAllTotalSum(String userId);
    public double getMonthlyTotalSum(String userId);
    public double getLastMonthTotalSum(String userId);

    public boolean compareExpenseAmountWithMonthlyBudget(String userId);

}
