package com.web.pocketbuddy.service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.pocketbuddy.dto.*;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.GroupExpenseDocument;
import com.web.pocketbuddy.entity.document.PersonalExpenseDocument;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.payload.AddPersonalExpense;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.payload.RegisterGroupExpense;
import com.web.pocketbuddy.payload.RegisterUser;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
public class MapperUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static UserDocument toUserDocument(RegisterUser registerUser) {
        return UserDocument.builder()
                .userFirstName(registerUser.getUserFirstName())
                .userLastName(registerUser.getUserLastName())
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
            log.error(e.getMessage());
            return null;
        }

    }

    public static PersonalExpenseDocument convertToPersonalExpenseDocument(AddPersonalExpense expense) {
        return PersonalExpenseDocument.builder()
                .userId(expense.getUserId())
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
                .discoverableId(savedGroupDocument.getGroupDiscoverableId())
                .groupName(savedGroupDocument.getGroupName())
                .groupDescription(savedGroupDocument.getDescription())
                .createdByUserId(savedGroupDocument.getCreatedByUser())
                .createdAt(savedGroupDocument.getCreateDate())
                .joinedMembers(savedGroupDocument.getMembers())
                .build();

    }

    public static GroupDetailsDto toGroupDetailsDto(GroupExpenseDocument expense) {
        return GroupDetailsDto.builder()
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

    public static GroupExpenseDocument convertToGroupDetailsDocument(GroupDetailsDto groupDetailsDto) {
        return GroupExpenseDocument.builder()
                .groupId(groupDetailsDto.getGroupId())
                .expenseDescription(groupDetailsDto.getDescription())
                .expenseAmount(groupDetailsDto.getAmount())
                .includedMembers(new HashMap<>())
                .createdDate(groupDetailsDto.getCreateDate())
                .registerByUserId(groupDetailsDto.getPaidByUserId())
                .isDeleted(false)
                .isUpdated(false)
                .build();
    }

    public static GroupExpenseDto convertToGroupExpenseDto(GroupExpenseDocument expenseDocument) {
        return GroupExpenseDto.builder()
                .expenseId(expenseDocument.getExpenseId())
                .groupId(expenseDocument.getGroupId())
                .userId(expenseDocument.getRegisterByUserId())
                .description(expenseDocument.getExpenseDescription())
                .amount(expenseDocument.getExpenseAmount())
                .includedMembers(expenseDocument.getIncludedMembers())
                .edited(false)
                .createdAt(expenseDocument.getCreatedDate())
                .build();
    }

    public static UserDetailResponse UserDetailResponse(UserDocument userDocument) {
        return UserDetailResponse.builder()
                .userId(userDocument.getUserId())
                .userFirstName(userDocument.getUserFirstName())
                .userLastName(userDocument.getUserLastName())
                .email(userDocument.getEmail())
                .mobileNumber(userDocument.getMobileNumber())
                .emailVerified(userDocument.isEmailVerified())
                .build();
    }

    public static GroupExpenseDocument convertToGroupExpenseDocument(RegisterGroupExpense expensePayload) {
        return GroupExpenseDocument.builder()
                .groupId(expensePayload.getGroupId())
                .expenseDescription(expensePayload.getDescription())
                .expenseAmount(expensePayload.getAmount())

                .registerByUserId(expensePayload.getRegisterByUserId())
                .isUpdated(false)
                .isDeleted(false)
                .build();
    }

    public static <T> T convertStringToObject(String mapperString, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(mapperString, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static String maskedString(@Email String unMaskedString, boolean isEmail) {
        if (isEmail) {
            String[] splitString = unMaskedString.split("@");
            if (splitString.length != 2) {
                return unMaskedString;
            }
            String localPart = splitString[0];
            String domain = splitString[1];
            if (localPart.length() <= 2) {
                return localPart + "@****";
            }
            String maskedLocalPart = localPart.charAt(0) + "*".repeat(localPart.length() - 2) + localPart.charAt(localPart.length() - 1);
            return maskedLocalPart + "@" + domain;
        } else {
            if (unMaskedString.length() <= 2) {
                return "*".repeat(unMaskedString.length()); // Mask fully if too short
            }
            return unMaskedString.charAt(0) + "*".repeat(unMaskedString.length() - 2) + unMaskedString.charAt(unMaskedString.length() - 1);
        }
    }

}
