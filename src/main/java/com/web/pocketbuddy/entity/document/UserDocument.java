package com.web.pocketbuddy.entity.document;

import com.web.pocketbuddy.entity.helper.DeviceDetail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EnableMongoAuditing
public class UserDocument {

    @Id
    private String userId;
    @NotNull
    private String userFirstName;
    private String userLastName;
    private String mobileNumber;

    @NotNull
    private String username;
    @Email
    private String email;
    @NotNull
    private String password;

    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date lastUpdatedDate;

    private String oneTimePassword;
    private String emailVerificationToken;
    private String changePasswordToken;

    private boolean subscribeEmailNotification = true;

    private Date lastLoginDate;
    private String lastLoginIp;
    private String lastLoginDevice;
    private String currentModelName;
    private String currentModelVersion;
    private String currentOsVersion;
    private String currentAppVersion;

    // Id - token || value - Device Detail (Device Id - Model Name)
    Map<String, DeviceDetail> listOfLoginDevices;

    private List<String> userJoinGroupId;

    private double userMonthlyBudget;
    private double userMonthlyExpense;
    private double userMonthlyIncome;

    private double userYearlyBudget;
    private double userYearlyExpense;
    private double userYearlyIncome;

    private boolean showGroupExpenseInPersonalExpenseList = false;

    private boolean isEmailVerified = false;
    private boolean isPhoneVerified = false;
}
