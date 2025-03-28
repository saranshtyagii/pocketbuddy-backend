package com.web.pocketbuddy.service;

import com.web.pocketbuddy.dto.PersonalExpenseResponse;
import com.web.pocketbuddy.payload.AddPersonalExpense;
import com.web.pocketbuddy.payload.FetchByDates;

import java.util.List;

public interface PersonalExpenseService {

    public PersonalExpenseResponse addPersonalExpense(AddPersonalExpense expense);
    public PersonalExpenseResponse updatePersonalExpense(AddPersonalExpense expense);
    public String deletePersonalExpense(String id);
    public PersonalExpenseResponse getPersonalExpense(String id);
    public void deletePersonalExpenseFromDb(String id);
    public List<PersonalExpenseResponse> getPersonalExpensesInRange(FetchByDates fetchByDates);

    public List<PersonalExpenseResponse> fetchAllPersonalExpense(String usernameOrEmail);
    public List<PersonalExpenseResponse> fetchAllPersonalExpenses();
    public List<PersonalExpenseResponse> fetchAllPersonalExpensesByUserId(String userId);

    public double getAllTotalSum(String usernameOrEmail);
    public double getMonthlyTotalSum(String usernameOrEmail);
    public double getLastMonthTotalSum(String usernameOrEmail);

    public boolean compareExpenseAmountWithMonthlyBudget(String usernameOrEmail);


}
