package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.dto.PersonalExpenseResponse;
import com.web.pocketbuddy.entity.dao.GroupExpenseMasterDao;
import com.web.pocketbuddy.entity.dao.PersonalExpenseMasterDao;
import com.web.pocketbuddy.entity.document.GroupExpenseDocument;
import com.web.pocketbuddy.entity.document.PersonalExpenseDocument;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.exception.GroupApiExceptions;
import com.web.pocketbuddy.exception.UserApiException;
import com.web.pocketbuddy.exception.UserPersonalExpenseException;
import com.web.pocketbuddy.payload.AddPersonalExpense;
import com.web.pocketbuddy.payload.FetchByDates;
import com.web.pocketbuddy.payload.FindExpenseByDates;
import com.web.pocketbuddy.service.PersonalExpenseService;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import com.web.pocketbuddy.utils.ConfigService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class PersonalExpenseResponseService implements PersonalExpenseService {

    private final PersonalExpenseMasterDao personalExpenseMasterDoa;
    private final UserService userService;
    private final GroupExpenseMasterDao groupExpenseMasterDao;

    @Override
    public PersonalExpenseResponse addPersonalExpense(AddPersonalExpense expense) {
        if(ObjectUtils.isEmpty(expense.getCreatedDate())) {
            expense.setCreatedDate(new Date());
        }
        PersonalExpenseDocument personalExpenseDocument = MapperUtils.convertToPersonalExpenseDocument(expense);
        return MapperUtils.convertTOPersonalExpenseResponse(personalExpenseMasterDoa.save(personalExpenseDocument));
    }

    @Override
    public PersonalExpenseResponse updatePersonalExpense(AddPersonalExpense expense) {
        PersonalExpenseDocument savedPersonalExpenseDocument = fetchPersonalExpenseDocumentById(expense.getExpenseId());

        savedPersonalExpenseDocument.setExpenseDescription(expense.getDescription());
        savedPersonalExpenseDocument.setExpenseId(expense.getExpenseId());
        savedPersonalExpenseDocument.setAmount(expense.getAmount());
        savedPersonalExpenseDocument.setExpenseDate(expense.getCreatedDate());

        personalExpenseMasterDoa.save(savedPersonalExpenseDocument);

        return MapperUtils.convertTOPersonalExpenseResponse(savedPersonalExpenseDocument);
    }

    @Override
    public String markExpenseAsDeleted(String expenseId) {
        PersonalExpenseDocument personalExpenseDocument = fetchPersonalExpenseDocumentById(expenseId);
        personalExpenseDocument.setDeleted(true);
        personalExpenseMasterDoa.save(personalExpenseDocument);
        return "Expense has been deleted";
    }

    @Override
    public String deletePersonalExpenseFromDb(String apiKey, String expenseId) {

        if(!apiKey.equalsIgnoreCase(ConfigService.getConfig().getApiKey())) {
            throw new UserApiException("Invalid Api Key", HttpStatus.UNAUTHORIZED);
        }

        if(!ObjectUtils.isEmpty(expenseId)) {
            personalExpenseMasterDoa.deleteById(expenseId);
            return "Expense has been deleted";
        }

        List<PersonalExpenseDocument> savedExpense = personalExpenseMasterDoa.findAll();
        if(CollectionUtils.isEmpty(savedExpense)) {
            return "Expense not found";
        }
        AtomicInteger deleteCount = new AtomicInteger();
        savedExpense.parallelStream().forEach(personalExpenseDocument -> {
            if(personalExpenseDocument.isDeleted()) {
                personalExpenseMasterDoa.delete(personalExpenseDocument);
                deleteCount.getAndIncrement();
            }
        });
        return deleteCount+" Expense has been deleted from database";
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
    public List<PersonalExpenseResponse> getPersonalExpensesInRange(FetchByDates data) {

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
                .orElse(Collections.emptyList());

        if(ObjectUtils.isEmpty(savedPersonalExpense)) {
            return Collections.emptyList();
        }

        return savedPersonalExpense.stream()
                .filter(expense -> !expense.isDeleted())
                .map(MapperUtils::convertTOPersonalExpenseResponse)
                .toList();
    }

    @Override
    public double getAllTotalSum(String userId) {
        List<PersonalExpenseDocument> savedPersonalExpense = personalExpenseMasterDoa.findByUserId(userId)
                .orElse(null);

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
        List<PersonalExpenseDocument> savedExpenses = fetchPersonalExpenseByUserId(userId, false);
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
        List<PersonalExpenseDocument> savedExpenses = fetchPersonalExpenseByUserId(userId, false);
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

    @Override
    public List<PersonalExpenseResponse> fetchAllExpenseByDates(FindExpenseByDates details) {
        if(StringUtils.isEmpty(details.getUserId())) {
            throw new UserApiException("User Id can't be empty", HttpStatus.NOT_FOUND);
        }

        List<PersonalExpenseDocument> savedExpenses = fetchAllExpensesByUserId(details.getUserId());
        if(ObjectUtils.isEmpty(savedExpenses)) {
            return Collections.emptyList();
        }

        return savedExpenses.stream()
                .filter(expense ->
                        expense.getExpenseDate().after(details.getStartDate())
                                && expense.getExpenseDate().before(details.getEndDate()))
                .map(MapperUtils::convertTOPersonalExpenseResponse)
                .toList();
    }

    private List<PersonalExpenseDocument> fetchAllExpensesByUserId(String userId) {

        List<PersonalExpenseDocument> savedExpenses = personalExpenseMasterDoa.findAllByUserId(userId).orElse(null);
        if(ObjectUtils.isEmpty(savedExpenses)) {
            return Collections.emptyList();
        }

        return savedExpenses.stream()
                .filter(expense -> !expense.isDeleted())
                .toList();
    }

    private PersonalExpenseDocument fetchPersonalExpenseDocumentById(String expenseId) {
        return personalExpenseMasterDoa.findById(expenseId)
                .orElseThrow(() -> new UserPersonalExpenseException("No Such Expense Found", HttpStatus.NOT_FOUND));
    }

    private List<PersonalExpenseDocument> fetchPersonalExpenseByUserId(String userId, boolean checkForNuls) {
        if(checkForNuls) {
            return personalExpenseMasterDoa.findByUserId(userId)
                    .orElseThrow(() -> new UserPersonalExpenseException("No Expense Found", HttpStatus.NOT_FOUND));
        }
        return personalExpenseMasterDoa.findByUserId(userId)
                .orElse(null);
    }

}
