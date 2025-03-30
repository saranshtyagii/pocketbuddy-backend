package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.constants.ConstantsVariables;
import com.web.pocketbuddy.dto.PersonalExpenseResponse;
import com.web.pocketbuddy.entity.dao.PersonalExpenseMasterDoa;
import com.web.pocketbuddy.entity.document.PersonalExpenseDocument;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.exception.UserPersonalExpenseException;
import com.web.pocketbuddy.payload.AddPersonalExpense;
import com.web.pocketbuddy.payload.FetchByDates;
import com.web.pocketbuddy.payload.FindExpenseByDates;
import com.web.pocketbuddy.service.PersonalExpenseService;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalExpenseResponseService implements PersonalExpenseService {

    private final PersonalExpenseMasterDoa personalExpenseMasterDoa;
    private final UserService userService;

    @Override
    public PersonalExpenseResponse addPersonalExpense(AddPersonalExpense expense) {
        PersonalExpenseDocument personalExpenseDocument = MapperUtils.convertToPersonalExpenseDocument(expense);
        PersonalExpenseDocument savedExpense = personalExpenseMasterDoa.save(personalExpenseDocument);
        return MapperUtils.convertTOPersonalExpenseResponse(savedExpense);
    }

    @Override
    public PersonalExpenseResponse updatePersonalExpense(AddPersonalExpense expense) {
        PersonalExpenseDocument savedPersonalExpenseDocument = fetchPersonalExpenseDocumentById(expense.getExpenseID());

        savedPersonalExpenseDocument.setExpenseDescription(expense.getDescription());
        savedPersonalExpenseDocument.setExpenseId(expense.getExpenseID());
        savedPersonalExpenseDocument.setAmount(expense.getAmount());
//        savedPersonalExpenseDocument.setExpenseDate(expense.getCreatedDate());

        personalExpenseMasterDoa.save(savedPersonalExpenseDocument);

        return MapperUtils.convertTOPersonalExpenseResponse(savedPersonalExpenseDocument);
    }

    @Override
    public String deletePersonalExpense(String expenseId) {
        PersonalExpenseDocument personalExpenseDocument = fetchPersonalExpenseDocumentById(expenseId);
        personalExpenseDocument.setDeleted(true);
        personalExpenseMasterDoa.save(personalExpenseDocument);
        return "Expense has been deleted";
    }

    @Override
    public String deletePersonalExpenseFromDB(String apiKey, String expenseId) {
        if(!apiKey.equals(ConstantsVariables.API_KEY)) {
            throw new UserPersonalExpenseException("Invalid Api Key", HttpStatus.BAD_REQUEST);
        }

        if(!StringUtils.isEmpty(expenseId)) {
            personalExpenseMasterDoa.delete(personalExpenseMasterDoa.findById(expenseId).orElseThrow(() -> new UserPersonalExpenseException("Expense not found", HttpStatus.NOT_FOUND)));
            return "Expense ID: "+expenseId+" has been deleted";
        }

        List<PersonalExpenseDocument> savedPersonalExpenseDocuments = personalExpenseMasterDoa.findAll();
        if(CollectionUtils.isEmpty(savedPersonalExpenseDocuments)) {
            return "There is no personal expense to be deleted";
        }

        savedPersonalExpenseDocuments.parallelStream().forEach(expense -> {
            if(expense.isDeleted()) {
                personalExpenseMasterDoa.delete(expense);
            }
        });
        return "All Expense has been deleted which marked as deleted";
    }

    @Override
    public PersonalExpenseResponse getPersonalExpense(String id) {
        PersonalExpenseDocument personalExpenseDocument = fetchPersonalExpenseDocumentById(id);
        if(personalExpenseDocument.isDeleted()) {
            throw new UserPersonalExpenseException("No such expense found!", HttpStatus.NOT_FOUND);
        }
        return MapperUtils.convertTOPersonalExpenseResponse(personalExpenseDocument);
    }

    @Override
    public List<PersonalExpenseResponse> getPersonalExpensesInRange(FindExpenseByDates datesData) {

        return List.of();
    }

    @Override
    public List<PersonalExpenseResponse> fetchAllPersonalExpense(String usernameOrEmail) {
        return List.of();
    }

    @Override
    public List<PersonalExpenseResponse> fetchAllPersonalExpenses() {
        List<PersonalExpenseDocument> allPersonalExpenseDocuments = personalExpenseMasterDoa.findAll();
        return allPersonalExpenseDocuments.stream()
                .filter((expense -> !expense.isDeleted()))
                .map(MapperUtils::convertTOPersonalExpenseResponse)
                .toList();
    }

    @Override
    public List<PersonalExpenseResponse> fetchAllPersonalExpensesByUserId(String userId) {
        List<PersonalExpenseDocument> savedPersonalExpense = personalExpenseMasterDoa.findAllByUserId(userId)
                .orElse(null);

        if(ObjectUtils.isEmpty(savedPersonalExpense)) {
            return null;
        }

        return savedPersonalExpense.stream()
                .filter(expense -> !expense.isDeleted())
                .map(MapperUtils::convertTOPersonalExpenseResponse)
                .toList();
    }

    @Override
    public double getAllTotalSum(String userId) {
        List<PersonalExpenseDocument> savedPersonalExpense = personalExpenseMasterDoa.findAllByUserId(userId).orElse(null);

        if(ObjectUtils.isEmpty(savedPersonalExpense)) {
            return 0.0;
        }

        return savedPersonalExpense.stream()
                .filter(expense -> !expense.isDeleted())
                .mapToDouble(PersonalExpenseDocument::getAmount)
                .sum();
    }

    @Override
    public double getMonthlyTotalSum(String userId) {
        List<PersonalExpenseDocument> savedExpenses = findAllByUserId(userId);
        if (ObjectUtils.isEmpty(savedExpenses)) {
            return 0.0;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfMonth = calendar.getTime();

        return savedExpenses.stream()
                .filter(expense -> !expense.isDeleted())
                .filter(expense -> expense.getExpenseDate().after(startOfMonth))
                .mapToDouble(PersonalExpenseDocument::getAmount)
                .sum();
    }


    @Override
    public double getLastMonthTotalSum(String userId) {
        List<PersonalExpenseDocument> savedExpenses = findAllByUserId(userId);
        if (ObjectUtils.isEmpty(savedExpenses)) {
            return 0.0;
        }

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH,  today.getDay());
        Date lastMonth = calendar.getTime();

        return  savedExpenses.stream()
                .filter(expense -> !expense.isDeleted())
                .filter(expense -> expense.getExpenseDate().after(lastMonth))
                .mapToDouble(PersonalExpenseDocument::getAmount)
                .sum();
    }

    @Override
    public boolean compareExpenseAmountWithMonthlyBudget(String userId) {
        double sum = getLastMonthTotalSum(userId);
        UserDocument savedUserResponse = userService.findUserById(userId);
        return sum < savedUserResponse.getUserMonthlyExpense();
    }

    private PersonalExpenseDocument fetchPersonalExpenseDocumentById(String expenseId) {
        return personalExpenseMasterDoa.findById(expenseId)
                .orElseThrow(() -> new UserPersonalExpenseException("No Such Expense Found", HttpStatus.NOT_FOUND));
    }

    private List<PersonalExpenseDocument> findAllByUserId(String userId) {
        List<PersonalExpenseDocument> savedExpense = personalExpenseMasterDoa.findAllByUserId(userId).orElse(null);
        if(ObjectUtils.isEmpty(savedExpense)) {
            return null;
        }

        return savedExpense.stream().filter(expense -> !expense.isDeleted()).collect(Collectors.toList());

    }

}
