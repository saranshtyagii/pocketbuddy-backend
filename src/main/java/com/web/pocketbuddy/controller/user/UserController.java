package com.web.pocketbuddy.controller.user;

import com.web.pocketbuddy.constants.UrlsConstants;
import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlsConstants.USER_URL)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/find")
    public ResponseEntity<UserDetailResponse> fetchUserDetail(@RequestParam String usernameOrEmail) {
        return ResponseEntity.ok(userService.findUserByUsername(usernameOrEmail));
    }

    @PutMapping("/update")
    public ResponseEntity<UserDetailResponse> updateUserDetails(@RequestBody UserDetailResponse userDetailResponse) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/delete")
    public ResponseEntity<UserDetailResponse> deleteUserDetails(@RequestBody UserDetailResponse userDetailResponse) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/phone")
    public ResponseEntity<String> updatePhoneNumber(@RequestParam String mobileNumber, @RequestParam String usernameOrEmail) {
        return ResponseEntity.ok(userService.updateMobileNumber(mobileNumber, usernameOrEmail));
    }

    @PatchMapping("/verify/phone")
    public ResponseEntity<String> verifyPhoneNumber(@RequestParam String phoneNumber, @RequestParam String otp) {
        return ResponseEntity.ok(userService.verifyMobileNumber(phoneNumber, otp));
    }

}
