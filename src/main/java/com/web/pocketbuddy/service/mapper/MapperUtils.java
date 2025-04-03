package com.web.pocketbuddy.service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.pocketbuddy.dto.*;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.GroupExpenseDocument;
import com.web.pocketbuddy.entity.document.PersonalExpenseDocument;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.payload.AddPersonalExpense;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.payload.RegisterUser;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

public class MapperUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static UserDocument toUserDocument(RegisterUser registerUser) {
        return UserDocument.builder()
                .userFirstName(registerUser.getUserFirstName())
                .userLastName(registerUser.getUserLastName())
                .username(registerUser.getUsername())
                .email(registerUser.getEmail())
                .mobileNumber(registerUser.getMobileNumber())
                .subscribeEmailNotification(true) // Default value
                .showGroupExpenseInPersonalExpenseList(false) // Default value
                .build();
    }

    public static UserDetailResponse toUserDetailResponse(UserDocument userDocument) {
        return UserDetailResponse.builder()
                .userId(userDocument.getUserId())
                .userFirstName(userDocument.getUserFirstName())
                .userLastName(userDocument.getUserLastName())
                .username(userDocument.getUsername())
                .email(userDocument.getEmail())
                .mobileNumber(userDocument.getMobileNumber())
                .build();
    }

    public static String convertObjectToString(Object response) {
        if(ObjectUtils.isEmpty(response)) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return null;
        }

    }

    public static PersonalExpenseDocument convertToPersonalExpenseDocument(AddPersonalExpense expense) {
        return PersonalExpenseDocument.builder()
                .userId(expense.getUserID())
                .expenseDescription(expense.getDescription())
                .amount(expense.getAmount())
                .isUpdated(false)
                .isDeleted(false)
                .isExpenseFromGroup(false)
                .amount(expense.getAmount())
                .build();
    }

    public static PersonalExpenseResponse convertTOPersonalExpenseResponse(PersonalExpenseDocument save) {
        return PersonalExpenseResponse.builder()
                .expenseId(save.getExpenseId())
                .description(save.getExpenseDescription())
                .amount(save.getAmount())
                .expenseDate(save.getExpenseDate())
                .updatedDate(save.getLastModifiedDate())
                .isEdited(save.isUpdated())
                .isDeleted(save.isDeleted())
                .build();
    }

    public static GroupDocument convertToGroupDocument(GroupRegisterDetails expense) {

        GroupDocument groupDocument = GroupDocument.builder()
                .groupName(expense.getGroupName())
                .description(expense.getDescription())
                .createdByUser(expense.getCreatedByUser())
                .groupBudget(expense.getGroupBudget())
                .groupBudget(expense.getGroupBudget())
                .build();

        if(expense.getBudgetPerDay() != 0) {
            groupDocument.setBudgetPerDay(expense.getBudgetPerDay());
        }

        if(expense.getTripStartDate() != null && expense.getTripEndDate() != null) {
            groupDocument.setTripStartDate(expense.getTripStartDate());
            groupDocument.setTripEndDate(expense.getTripEndDate());
        }
        return groupDocument;
    }

    public static GroupDetailsResponse convertGroupDetailResponse(GroupDocument savedGroupDocument) {
        return GroupDetailsResponse.builder()
                .groupId(savedGroupDocument.getGroupId())
                .groupName(savedGroupDocument.getGroupName())
                .groupDescription(savedGroupDocument.getDescription())
                .createdByUserId(savedGroupDocument.getCreatedByUser())
                .createdDate(savedGroupDocument.getCreateDate())
                .joinedMembersWithExpenseAmount(savedGroupDocument.getJoinedMembersWithExpenseAmount())
                .build();

    }

    public static GroupExpensesDto toGroupExpensesDto(GroupExpenseDocument expense) {
        return GroupExpensesDto.builder()
                .expenseId(expense.getExpenseId())
                .groupId(expense.getGroupId())
                .description(expense.getExpenseDescription())
                .amount(expense.getExpenseAmount())
                .createDate(expense.getCreatedDate())
                .lastUpdateDate(expense.getUpdatedDate())
                .isUpdated(expense.isUpdated())
                .paidByUserId(expense.getRegisterByUserId())
                .includedMembers(new ArrayList<>())
                .build();
    }

    public static GroupExpenseDocument convertToGroupExpenseDocument(GroupExpensesDto groupExpensesDto) {
        return GroupExpenseDocument.builder()
                .groupId(groupExpensesDto.getGroupId())
                .expenseDescription(groupExpensesDto.getDescription())
                .expenseAmount(groupExpensesDto.getAmount())
                .includedMembers(new ArrayList<>())
                .createdDate(groupExpensesDto.getCreateDate())
                .registerByUserId(groupExpensesDto.getPaidByUserId())
                .isDeleted(false)
                .isUpdated(false)
                .build();
    }
}
