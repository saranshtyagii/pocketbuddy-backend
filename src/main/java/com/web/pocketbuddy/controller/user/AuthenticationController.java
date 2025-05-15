package com.web.pocketbuddy.controller.user;

import com.web.pocketbuddy.constants.UrlsConstants;
import com.web.pocketbuddy.dto.TokenResponse;
import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.exception.UserApiException;
import com.web.pocketbuddy.payload.RegisterUser;
import com.web.pocketbuddy.payload.UpdatePasswordPayload;
import com.web.pocketbuddy.payload.UserCredentials;
import com.web.pocketbuddy.security.JwtTokenUtils;
import com.web.pocketbuddy.security.JwtUserDetailService;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlsConstants.AUTH_URL)
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailService userDetailService;
    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody UserCredentials userCredentials) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userCredentials.getEmail(), userCredentials.getPassword())
            );
        } catch (Exception e) {
            throw new UserApiException("Invalid password", HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = userDetailService.loadUserByUsername(userCredentials.getEmail());
        TokenResponse tokenResponse = new TokenResponse(jwtTokenUtils.generateToken(userDetails));

        userService.saveUserTokenAndData(userCredentials, tokenResponse.getToken());

        return ResponseEntity.ok(MapperUtils.convertObjectToString(tokenResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUser registerUser) {
        UserDetailResponse response = userService.registerUser(registerUser);
        return ResponseEntity.ok(MapperUtils.convertObjectToString(response));
    }

    @PatchMapping("/verify/email/otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String usernameOrEmail, @RequestParam String otp) {
//        return ResponseEntity.ok(userService.verifyMobileNumber(usernameOrEmail, otp));
        return null;
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String usernameOrEmail) {
        return ResponseEntity.ok(userService.generateChangePasswordToken(usernameOrEmail));
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordPayload passwordPayload) {
        return ResponseEntity.ok(userService.updatePassword(passwordPayload.getToken(), passwordPayload.getNewPassword()));
    }

    @PatchMapping("/generate/otp")
    public ResponseEntity<String> generateOtpForPhone(@RequestParam String mobileNumber) {
        return ResponseEntity.ok(userService.generateOneTimePasswordForMobile(mobileNumber));
    }

    @PostMapping("/login/phone")
    public ResponseEntity<UserDetailResponse> loginByPhone(@RequestBody String mobileNumber) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/re-verify/email")
    public ResponseEntity<String> reVerifyEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.sendEmailVerificationLink(email));
    }

    @GetMapping("/verify/email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        String email = userService.verifyEmailWithToken(token);
        if(StringUtils.isNotBlank(email)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/template/email-verification-successfully?email=" + email));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        return ResponseEntity.ok().build();
    }

}
