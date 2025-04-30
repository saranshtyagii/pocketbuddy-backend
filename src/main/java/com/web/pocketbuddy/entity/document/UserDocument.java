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

    // === User Identity ===
    @NotNull
    private String userFirstName;
    private String userLastName;

    @Email
    private String email;
    private String mobileNumber;
    private Boolean loginWithMobile;

    // === Security & Authentication ===
    private String password;
    private String oneTimePassword;
    private String emailVerificationToken;
    private String changePasswordToken;
    private boolean isEmailVerified = false;
    private boolean isPhoneVerified = false;

    // === Timestamps ===
    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date lastUpdatedDate;

    // === Login Tracking ===
    private Date lastLoginDate;
    private String lastLoginIp;
    private String lastLoginDevice;
    private Map<String, DeviceDetail> listOfLoginDevices;

    // === Current Device Info ===
    private String currentModelName;
    private String currentModelVersion;
    private String currentOsVersion;
    private String currentAppVersion;

    // === Notification Preferences ===
    private boolean subscribeEmailNotification = true;

    // === Group Membership ===
    private List<String> userJoinGroupId;
    private boolean showGroupExpenseInPersonalExpenseList = false;

    // === Financial Data ===
    private double userMonthlyBudget;
    private double userMonthlyExpense;
    private double userMonthlyIncome;

    private double userYearlyBudget;
    private double userYearlyExpense;
    private double userYearlyIncome;
}
