package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.constants.*;
import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.entity.dao.UserMasterDao;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.entity.helper.DeviceDetail;
import com.web.pocketbuddy.utils.GenerateUtils;
import com.web.pocketbuddy.exception.UserApiException;
import com.web.pocketbuddy.payload.RegisterUser;
import com.web.pocketbuddy.payload.UserCredentials;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import com.web.pocketbuddy.service.notification.NotificationService;
import com.web.pocketbuddy.utils.RedisServices;
import com.web.pocketbuddy.utils.RedisUtils;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserResponseService implements UserService {

    private final UserMasterDao userMasterDoa;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final RedisServices redisServices;

    @Override
    public UserDetailResponse registerUser(RegisterUser registerUser) {

        if(!isEmailExist(registerUser.getEmail())) {
            throw new UserApiException(ConstantsVariables.EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        if(!StringUtils.isEmpty(registerUser.getMobileNumber()) &&
            !isMobileNumberExist(registerUser.getMobileNumber())) {
            throw new UserApiException(ConstantsVariables.MOBILE_NUMBER_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
        }

        UserDocument requestedUser = MapperUtils.toUserDocument(registerUser);
        requestedUser.setLoginWithMobile(false);
        if(registerUser.getPassword().length() < ConstantsVariables.MINIMUM_PASSWORD_LENGTH) {
            throw new UserApiException(ConstantsVariables.WEEK_PASSWORD, HttpStatus.BAD_REQUEST);
        }
        requestedUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));

        UserDocument savedUser = userMasterDoa.save(requestedUser);

        String emailVerificationUrl = buildEmailVerificationUrl(savedUser.getEmail());
        notificationService.sendEmail(savedUser.getEmail(), "Verify Email Address", NotificationTemplate.EMAIL_VERIFICATION, NotificationTemplate.EMAIL_VALUE_REPLACE_KEY, emailVerificationUrl);

        return MapperUtils.toUserDetailResponse(savedUser);
    }

    private String buildEmailVerificationUrl(String email) {

        String previousToken = redisServices.get(email);
        if(StringUtils.isEmpty(previousToken)) {
            String token = GenerateUtils.generateToken();
            String key = RedisUtils.EMAIL_VERIFICATION_TOKEN_KEY + token;
            redisServices.set(key, email, 60 * 5L);
            previousToken = token;
        }
        return UrlsConstants.HOST_HTTP_BASE_URL + UrlsConstants.AUTH_URL + "/verify/email?token=" + previousToken;
    }

    @Override
    public UserDetailResponse findUserByEmail(String email) {
        UserDocument userDocument = null;
        // find the data in redis
        userDocument = fetchUserFromCache(email);
        if(ObjectUtils.isEmpty(userDocument)) {
            userDocument = fetchUserByEmail(email);
            updateUserInCache(userDocument);
        }
        return MapperUtils.UserDetailResponse(userDocument);
    }

    @Override
    public UserDetailResponse findUserByPhone(String phone) {
        return null;
    }

    @Override
    public UserDocument findUserById(String id) {
        return userMasterDoa.findById(id)
                .orElseThrow(() -> new UserApiException(ConstantsVariables.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public void savedUpdatedUser(UserDocument userDocument) {
        if(ObjectUtils.isEmpty(userDocument)) {
            throw new UserApiException("User Data can't be empty!", HttpStatus.BAD_REQUEST);
        }
        userMasterDoa.save(userDocument);
    }

    @Override
    public String generateOneTimePasswordForEmail(String email) {
        return "";
    }


    @Override
    public String generateOneTimePasswordForMobile(String mobileNumber) {
        UserDocument savedUser = userMasterDoa.findByMobileNumber(mobileNumber)
                .orElse(null);

        if(ObjectUtils.isEmpty(savedUser)) {
            saveAndGenerateOneTimePasswordForMobile(mobileNumber);
        } else {
            resendOneTimePasswordForMobile(mobileNumber, null);
        }

        return ConstantsVariables.OTP_SEND_MESSAGE + maskedString(savedUser.getMobileNumber(), false);
    }

    private void saveAndGenerateOneTimePasswordForMobile(String mobileNumber) {

        UserDocument userDocument = new UserDocument();
        userDocument.setUserFirstName("Pocket Buddy");
        userDocument.setLoginWithMobile(true);
        userDocument.setPassword(passwordEncoder.encode("pocketbuddy_"+mobileNumber+"_password"));
        String otp = GenerateUtils.generateOtp(mobileNumber);

        String key = RedisUtils.OTP_VERIFICATION_KEY + mobileNumber;
        redisServices.set(key, otp, 60 * 5L);

        userMasterDoa.save(userDocument);
        resendOneTimePasswordForMobile(mobileNumber, otp);
    }

    private void resendOneTimePasswordForMobile(String mobileNumber, String otp) {
        if(StringUtils.isEmpty(otp)) {
            String otpCode = GenerateUtils.generateOtp(mobileNumber);
        }


    }

    @Override
    public String verifyMobileNumber(String phoneNumber, String otp) {
        return "";
    }

    @Override
    public void saveUserTokenAndData(UserCredentials userCredentials, String token) {
        try {
            UserDocument savedUser = fetchUserByEmail(userCredentials.getEmail());

            // Update basic user login data
            savedUser.setLastLoginDate(new Date());
            savedUser.setLastLoginIp(userCredentials.getIpAddress());
            savedUser.setCurrentModelName(userCredentials.getModelName());
            savedUser.setCurrentModelVersion(userCredentials.getModelVersion());
            savedUser.setCurrentOsVersion(userCredentials.getOsVersion());
            savedUser.setCurrentAppVersion(userCredentials.getAppVersion());

            // Update device details
            Map<String, DeviceDetail> listOfDevices = savedUser.getListOfLoginDevices();
            if (listOfDevices == null) {
                listOfDevices = new HashMap<>();
            }

            DeviceDetail deviceDetail = DeviceDetail.builder()
                    .loginDate(new Date())
                    .loginIp(userCredentials.getIpAddress())
                    .loginDeviceId(userCredentials.getDeviceId())
                    .modelName(userCredentials.getModelName())
                    .modelVersion(userCredentials.getModelVersion())
                    .osVersion(userCredentials.getOsVersion())
                    .authToken(token)
                    .build();

            String sanitizedDeviceId = StringUtils.replace(deviceDetail.getLoginDeviceId(), ".", "_");
            listOfDevices.put(sanitizedDeviceId, deviceDetail);

            savedUser.setListOfLoginDevices(listOfDevices);

            userMasterDoa.save(savedUser);

        } catch (Exception e) {
            // You can optionally log the stack trace here for debugging
             e.printStackTrace();
            throw new UserApiException("Something went wrong while saving user token and data", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String generateChangePasswordToken(String email) {
        UserDocument savedUser = userMasterDoa.findByEmail(email)
                .orElseThrow(() -> new UserApiException(ConstantsVariables.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        if(!savedUser.isEmailVerified()) throw new UserApiException("Email is not verified", HttpStatus.BAD_REQUEST);

        Object savedToken = null;
        try {
            savedToken = redisServices.get("change-password-token:" + savedUser.getUserId());
            if(ObjectUtils.isEmpty(savedToken)) {
                savedToken = GenerateUtils.generateToken();
                savedUser.setChangePasswordToken(savedToken.toString());
                redisServices.set(email, savedToken,5L * 60);
                savedUpdatedUser(savedUser);
            }
        } catch (Exception e) {
            savedToken = GenerateUtils.generateToken();
            savedUser.setChangePasswordToken(savedToken.toString());
            redisServices.set(email, savedToken);
            savedUpdatedUser(savedUser);
        }

        String restLink = UrlsConstants.HOST_HTTP_BASE_URL + "/template/change-password?token=" + savedUser.getChangePasswordToken();
        notificationService.sendEmail(savedUser.getEmail(), "Pocket Buddy Update Password", NotificationTemplate.CHANGE_PASSWORD, "{{reset_link}}", restLink);

        return "Email has been send for change password on: " + MapperUtils.maskEmail(savedUser.getEmail());
    }

    @Override
    public String updatePassword(String token, String newPassword) {
        UserDocument savedUserDoc = userMasterDoa.findByChangePasswordToken(token)
                .orElseThrow(() ->
                        new UserApiException(ConstantsVariables.NO_SUCH_USER_FOUND, HttpStatus.BAD_REQUEST));
        savedUserDoc.setPassword(passwordEncoder.encode(newPassword));
        savedUserDoc.setChangePasswordToken(null);
        savedUpdatedUser(savedUserDoc);

        // clear redis key
        try {
            redisServices.delete("change-password-token:" + savedUserDoc.getUserId());
        } catch (Exception e) {
            // ignore
        }

        return "Password changed successfully";
    }


    @Override
    public String updateMobileNumber(String mobileNumber, String email) {
        UserDocument savedUser = fetchUserByEmail(email);
//        savedUser.setOneTimePassword(GenerateUtils.generateOtp(email));
        userMasterDoa.save(savedUser);
        return ConstantsVariables.OTP_SEND_MESSAGE + maskedString(savedUser.getMobileNumber(), false);
    }


    @Override
    public void deleteUserFromDb(String userID) {
        userMasterDoa.delete(userMasterDoa.findById(userID).orElseThrow(() -> new UserApiException(ConstantsVariables.NO_SUCH_USER_FOUND, HttpStatus.BAD_REQUEST)));
    }

    @Override
    public boolean isEmailVerified(String email) {
        UserDocument userDocument = fetchUserByEmail(email);
        return userDocument.isEmailVerified();
    }

    @Override
    public String sendEmailVerificationLink(String email) {
        UserDocument savedUser = fetchUserByEmail(email);
        if(savedUser.isEmailVerified()) {
            throw new UserApiException("Your are already verified", HttpStatus.BAD_REQUEST);
        }
        String emailVerificationUrl = buildEmailVerificationUrl(savedUser.getEmail());
        notificationService.sendEmail(savedUser.getEmail(), "Verify Email Address", NotificationTemplate.EMAIL_VERIFICATION, NotificationTemplate.EMAIL_VALUE_REPLACE_KEY, emailVerificationUrl);
        return "Verification link has been sent";
    }

    @Override
    public String verifyEmailWithToken(String token) {
        String key = RedisUtils.EMAIL_VERIFICATION_TOKEN_KEY + token;
        String value;
        try {
            value = redisServices.get(key);
            redisServices.delete(key);
            if(value == null) throw new UserApiException(ConstantsVariables.NO_SUCH_USER_FOUND, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new UserApiException("Something went wrong while verifying email token", HttpStatus.BAD_REQUEST);
        }
        UserDocument savedDocument = fetchUserByEmail(value);
        if(savedDocument.isEmailVerified()) {
            throw new UserApiException("Email already verified", HttpStatus.BAD_REQUEST);
        }
        savedDocument.setEmailVerified(true);
        userMasterDoa.save(savedDocument);
        return savedDocument.getEmail();
    }


    private boolean isEmailExist(String email) {
        UserDocument savedUser = userMasterDoa.findByEmail(email).orElse(null);
        return ObjectUtils.isEmpty(savedUser);
    }

    private boolean isMobileNumberExist(String mobileNumber) {
        UserDocument savedUser = userMasterDoa.findByMobileNumber(mobileNumber).orElse(null);
        return ObjectUtils.isEmpty(savedUser);
    }

    private String maskedString(@Email String unMaskedString, boolean isEmail) {
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

    private UserDocument fetchUserByEmail(String email) {
        return userMasterDoa.findByEmail(email)
                .orElseThrow(() -> new UserApiException(ConstantsVariables.NO_SUCH_USER_FOUND, HttpStatus.BAD_REQUEST));
    }

    private UserDocument fetchUserFromCache(String email) {
        try {
            String userKey = RedisConstants.USER_REDIS_KEY + email;
            String mapperString = redisServices.get(userKey);
            if(StringUtils.isEmpty(mapperString)) {
                return null;
            }
            return MapperUtils.convertStringToObject(mapperString, UserDocument.class);
        } catch (Exception e) {
            return null;
        }
    }

    private void updateUserInCache(UserDocument userDocument) {
        try {
            String mapperString = MapperUtils.convertObjectToString(userDocument);
            String userKey = RedisConstants.USER_REDIS_KEY + userDocument.getEmail();
            redisServices.set(userKey, mapperString, 24 * 60 * 60L);
        } catch (Exception e) {
            // ignore
        }

    }

}
