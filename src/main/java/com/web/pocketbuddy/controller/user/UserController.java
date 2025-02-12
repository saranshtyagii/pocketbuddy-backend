package com.web.pocketbuddy.controller.user;

import com.web.pocketbuddy.constants.ConstantsUrls;
import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ConstantsUrls.USER_URL)
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
        return ResponseEntity.ok(userService.verifyPhoneNumber(phoneNumber, otp));
    }




}
