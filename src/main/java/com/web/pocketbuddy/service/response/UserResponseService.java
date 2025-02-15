package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.constants.ConstantsUrls;
import com.web.pocketbuddy.constants.ConstantsVariables;
import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.entity.dao.UserMasterDoa;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.entity.helper.DeviceDetail;
import com.web.pocketbuddy.entity.helper.OtpGenerateUtils;
import com.web.pocketbuddy.exception.UserApiException;
import com.web.pocketbuddy.payload.RegisterUser;
import com.web.pocketbuddy.payload.UserCredentials;
import com.web.pocketbuddy.security.JwtUserDetailService;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
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

    private final UserMasterDoa userMasterDoa;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailResponse registerUser(RegisterUser registerUser) {

        if(!isUsernameExist(registerUser.getUsername())) {
            throw new UserApiException(ConstantsVariables.USERNAME_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }
        if(!isEmailExist(registerUser.getEmail())) {
            throw new UserApiException(ConstantsVariables.EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        if(!StringUtils.isEmpty(registerUser.getMobileNumber()) &&
            !isMobileNumberExist(registerUser.getMobileNumber())) {
            throw new UserApiException(ConstantsVariables.MOBILE_NUMBER_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
        }

        UserDocument requestedUser = MapperUtils.toUserDocument(registerUser);
        if(registerUser.getPassword().length() < ConstantsVariables.MINIMUM_PASSWORD_LENGTH) {
            throw new UserApiException(ConstantsVariables.WEEK_PASSWORD, HttpStatus.BAD_REQUEST);
        }
        requestedUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));

        // TODO: Find the user join groups

        return MapperUtils.toUserDetailResponse(userMasterDoa.save(requestedUser));
    }

    @Override
    public UserDetailResponse findUserByUsername(String username) {
        return null;
    }

    @Override
    public UserDetailResponse findUserByEmail(String email) {
        return null;
    }

    @Override
    public UserDetailResponse findUserByPhone(String phone) {
        return null;
    }

    @Override
    public UserDetailResponse findUserById(String id) {
        UserDocument savedUser = userMasterDoa.findById(id)
                .orElseThrow(() -> new UserApiException(ConstantsVariables.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        return MapperUtils.toUserDetailResponse(savedUser);
    }

    @Override
    public String generateOneTimePassword(String usernameOrEmail) {
        UserDocument savedUser = fetchUserByUsernameOrEmail(usernameOrEmail);

        String otp = OtpGenerateUtils.generateOtp();
        savedUser.setOneTimePassword(otp);
        userMasterDoa.save(savedUser);

        // ToDo: Send email

        return ConstantsVariables.OTP_SEND_MESSAGE + maskedString(savedUser.getEmail(), true);
    }

    @Override
    public String verifyMobileNumber(String mobile) {
        return "";
    }

    @Override
    public String verifyEmailOtp(String usernameOrEmail, String otp) {
        UserDocument savedUser = fetchUserByUsernameOrEmail(usernameOrEmail);
        if(otp.equals(savedUser.getOneTimePassword())) {
            return "Opt verify successfully";
        }
        throw new UserApiException(ConstantsVariables.INVALID_OTP, HttpStatus.BAD_REQUEST);
    }

    @Override
    public UserDetailResponse updatePassword(UserCredentials userCredentials) {
        UserDocument savedUser = fetchUserByUsernameOrEmail(userCredentials.getUsernameOrEmail());
        savedUser.setPassword(passwordEncoder.encode(userCredentials.getPassword()));

        return MapperUtils.toUserDetailResponse(userMasterDoa.save(savedUser));
    }

    @Override
    public String generateOneTimePasswordForMobile(String mobileNumber) {
        UserDocument savedUser = userMasterDoa.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new UserApiException(ConstantsVariables.NO_SUCH_USER_FOUND, HttpStatus.BAD_REQUEST));

        String otp = OtpGenerateUtils.generateOtp();
        savedUser.setOneTimePassword(otp);

        // ToDo :: Send Otp to register mobile number

        return ConstantsVariables.OTP_SEND_MESSAGE + maskedString(savedUser.getMobileNumber(), false);
    }

    @Override
    public void saveUserTokenAndData(UserCredentials userCredentials, String token) {
        UserDocument savedUser = fetchUserByUsernameOrEmail(userCredentials.getUsernameOrEmail());

        savedUser.setLastLoginDate(new Date());
        savedUser.setLastLoginIp(userCredentials.getIpAddress());
        savedUser.setCurrentModelName(userCredentials.getModelName());
        savedUser.setCurrentModelVersion(userCredentials.getModelVersion());
        savedUser.setCurrentOsVersion(userCredentials.getOsVersion());
        savedUser.setCurrentAppVersion(userCredentials.getAppVersion());

        Map<String, DeviceDetail> listOfDevices = savedUser.getListOfLoginDevices();
        if(listOfDevices == null) {
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
        listOfDevices.put(deviceDetail.getLoginDeviceId(), deviceDetail);
        savedUser.setListOfLoginDevices(listOfDevices);


        userMasterDoa.save(savedUser);
    }

    @Override
    public String updateMobileNumber(String mobileNumber, String usernameOrEmail) {
        UserDocument savedUser = fetchUserByUsernameOrEmail(usernameOrEmail);
        savedUser.setOneTimePassword(OtpGenerateUtils.generateOtp());
        userMasterDoa.save(savedUser);
        return ConstantsVariables.OTP_SEND_MESSAGE + maskedString(savedUser.getMobileNumber(), false);
    }

    @Override
    public String verifyPhoneNumber(String mobileNumber, String otp) {
        UserDocument savedUser = userMasterDoa.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new UserApiException(ConstantsVariables.NO_SUCH_USER_FOUND, HttpStatus.BAD_REQUEST));

        if(savedUser.isPhoneVerified()) {
            return "Mobile is already verified";
        }

        if(otp.equals(savedUser.getOneTimePassword())) {
            savedUser.setPhoneVerified(true);
            return "Opt verify successfully";
        }
        else {
            throw new UserApiException(ConstantsVariables.INVALID_OTP, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isUsernameExist(String username) {
         UserDocument savedUser = userMasterDoa.findByUsername(username).orElse(null);
         return ObjectUtils.isEmpty(savedUser);
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

    private UserDocument fetchUserByUsernameOrEmail(String usernameOrEmail) {
        return userMasterDoa.findByEmailOrUsername(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UserApiException(ConstantsVariables.NO_SUCH_USER_FOUND, HttpStatus.BAD_REQUEST));
    }

}
