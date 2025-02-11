package com.web.pocketbuddy.controller.user;

import com.web.pocketbuddy.constants.ConstantsUrls;
import com.web.pocketbuddy.dto.UserDetailResponse;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ConstantsUrls.USER_URL)
public class UserController {

    @GetMapping("/find")
    public ResponseEntity<UserDetailResponse> fetchUserDetail(@RequestParam String usernameOrEmail) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<UserDetailResponse> updateUserDetails(@RequestBody UserDetailResponse userDetailResponse) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/delete")
    public ResponseEntity<UserDetailResponse> deleteUserDetails(@RequestBody UserDetailResponse userDetailResponse) {
        return ResponseEntity.ok().build();
    }



}
