package com.web.pocketbuddy.service;

import com.web.pocketbuddy.dto.PersonalResponseResponse;
import com.web.pocketbuddy.payload.AddPersonalExpense;
import com.web.pocketbuddy.payload.FetchByDates;

import java.util.List;

public interface PersonalExpenseService {

    public PersonalResponseResponse addPersonalExpense(AddPersonalExpense expense);
    public PersonalResponseResponse updatePersonalExpense(AddPersonalExpense expense);
    public String deletePersonalExpense(String id);
    public PersonalResponseResponse getPersonalExpense(String id);

    public List<PersonalResponseResponse> getPersonalExpensesInRange(FetchByDates fetchByDates);

    public List<PersonalResponseResponse> fetchAllPersonalExpense(String usernameOrEmail);
    public List<PersonalResponseResponse> fetchAllPersonalExpenses();
    public List<PersonalResponseResponse> fetchAllPersonalExpensesByUserId(String userId);

    public double getAllTotalSum(String usernameOrEmail);
    public double getMonthlyTotalSum(String usernameOrEmail);
    public double getLastMonthTotalSum(String usernameOrEmail);

    public boolean compareExpenseAmountWithMonthlyBudget(String usernameOrEmail);


}
