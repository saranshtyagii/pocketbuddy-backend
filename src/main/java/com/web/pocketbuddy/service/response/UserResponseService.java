package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.constants.ConstantsVariables;
import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.entity.dao.UserMasterDoa;
import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.UserDocument;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        requestedUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));

        // TODO: Find the user join groups

        return MapperUtils.toUserDetailResponse(userMasterDoa.save(requestedUser), new ArrayList<>());
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
        return null;
    }

    @Override
    public String generateOneTimePassword(String usernameOrEmail) {
        UserDocument savedUser = userMasterDoa.findByEmailOrUsername(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UserApiException(ConstantsVariables.NO_SUCH_USER_FOUND, HttpStatus.BAD_REQUEST));

        String otp = createSixDigitNumber();
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
        UserDocument savedUser = userMasterDoa.findByEmailOrUsername(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UserApiException(ConstantsVariables.NO_SUCH_USER_FOUND, HttpStatus.BAD_REQUEST));
        if(otp.equals(savedUser.getOneTimePassword())) {
            return "Opt verify successfully";
        }
        throw new UserApiException(ConstantsVariables.INVALID_OTP, HttpStatus.BAD_REQUEST);
    }

    @Override
    public UserDetailResponse updatePassword(UserCredentials userCredentials) {
        UserDocument savedUser = userMasterDoa.findByEmailOrUsername(userCredentials.getUsernameOrEmail(), userCredentials.getUsernameOrEmail())
                .orElseThrow(() -> new UserApiException(ConstantsVariables.NO_SUCH_USER_FOUND, HttpStatus.BAD_REQUEST));
        savedUser.setPassword(passwordEncoder.encode(userCredentials.getPassword()));

        return MapperUtils.toUserDetailResponse(userMasterDoa.save(savedUser), new ArrayList<>());
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

    private String createSixDigitNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
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

}
