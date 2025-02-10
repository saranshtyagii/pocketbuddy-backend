package com.web.pocketbuddy.service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.dto.UserJoinGroupResponse;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.payload.RegisterUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

public class MapperUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static UserDocument toUserDocument(RegisterUser registerUser) {
        return UserDocument.builder()
                .userFirstName(registerUser.getUserFirstName())
                .userLastName(registerUser.getUserLastName())
                .username(registerUser.getUsername())
                .email(registerUser.getEmail())
                .mobileNumber(registerUser.getMobileNumber())
                .createdDate(new Date()) // Set creation date
                .lastUpdatedDate(new Date()) // Set last updated date
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
            return null;
        }

    }
}
