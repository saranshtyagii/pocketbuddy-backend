package com.web.pocketbuddy.service;

import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.payload.RegisterUser;
import com.web.pocketbuddy.payload.UserCredentials;
import com.web.pocketbuddy.security.JwtUserDetailService;

public interface UserService {

    public UserDetailResponse registerUser(RegisterUser registerUser);
    public UserDetailResponse findUserByUsername(String username);
    public UserDetailResponse findUserByEmail(String email);
    public UserDetailResponse findUserByPhone(String phone);
    public UserDocument findUserById(String id);

    public UserDocument savedUpdatedUser(UserDocument userDocument);

    public String generateOneTimePassword(String usernameOrEmail);
    public String verifyMobileNumber(String mobile);

    public String verifyEmailOtp(String usernameOrEmail, String otp);

    public UserDetailResponse updatePassword(UserCredentials userCredentials);

    public String generateOneTimePasswordForMobile(String mobileNumber);

    public void saveUserTokenAndData(UserCredentials userCredentials, String token);

    public String updateMobileNumber(String mobileNumber, String usernameOrEmail);

    public String verifyPhoneNumber(String phoneNumber, String otp);
}
