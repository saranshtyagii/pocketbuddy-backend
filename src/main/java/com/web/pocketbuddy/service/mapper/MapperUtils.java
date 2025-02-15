package com.web.pocketbuddy.service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.pocketbuddy.dto.PersonalResponseResponse;
import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.dto.UserJoinGroupResponse;
import com.web.pocketbuddy.entity.document.PersonalExpenseDocument;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.payload.AddPersonalExpense;
import com.web.pocketbuddy.payload.RegisterUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
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

    public static UserDetailResponse toUserDetailResponse(UserDocument userDocument, List<UserJoinGroupResponse> joinGroups) {
        return UserDetailResponse.builder()
                .userId(userDocument.getUserId())
                .username(userDocument.getUsername())
                .email(userDocument.getEmail())
                .mobileNumber(userDocument.getMobileNumber())
                .personalExpense(joinGroups)
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
                .userId(expense.getUserID())
                .expenseDescription(expense.getDescription())
                .amount(expense.getAmount())
                .isUpdated(false)
                .isDeleted(false)
                .isExpenseFromGroup(false)
                .amount(expense.getAmount())
                .build();
    }

    public static PersonalResponseResponse convertTOPersonalExpenseResponse(PersonalExpenseDocument save) {
        return PersonalResponseResponse.builder()
                .expenseId(save.getExpenseId())
                .description(save.getExpenseDescription())
                .amount(save.getAmount())
                .expenseDate(save.getExpenseDate())
                .updatedDate(save.getLastModifiedDate())
                .isEdited(save.isUpdated())
                .isDeleted(save.isDeleted())
                .build();
    }
}
