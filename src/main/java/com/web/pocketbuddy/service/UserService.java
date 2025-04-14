package com.web.pocketbuddy.service;

import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.payload.RegisterUser;
import com.web.pocketbuddy.payload.UserCredentials;

import java.util.List;

public interface UserService {

    public UserDetailResponse registerUser(RegisterUser registerUser);
    public UserDetailResponse findUserByUsername(String username);
    public UserDocument findUserById(String id);
    public UserDetailResponse findUserByEmail(String email);
    public UserDetailResponse findUserByPhone(String phone);

    public void savedUpdatedUser(UserDocument userDocument);

    public String generateOneTimePasswordForEmail(String usernameOrEmail);
    public String generateOneTimePasswordForMobile(String mobileNumber);

    public String verifyMobileNumber(String phoneNumber, String otp);
    String verifyEmailWithToken(String token);


    public void saveUserTokenAndData(UserCredentials userCredentials, String token);

    public String generateChangePasswordToken(String usernameOrEmail);
    public String updatePassword(String token, String newPassword);
    public String updateMobileNumber(String mobileNumber, String usernameOrEmail);

    public void deleteUserFromDb(String id);


    List<GroupDetailsResponse> findUserJoinedGroups(String usernameOrEmail);
}
