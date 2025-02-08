package com.web.pocketbuddy.controller;

import com.web.pocketbuddy.constants.ConstantsUrls;
import com.web.pocketbuddy.constants.ConstantsVariables;
import com.web.pocketbuddy.entity.dao.UserMasterDoa;
import com.web.pocketbuddy.entity.document.UserDocument;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.Date;
import java.util.UUID;


@RestController()
@AllArgsConstructor
@RequestMapping(ConstantsUrls.BASE_URL_V1 + "/status")
public class ApiStatusController {

    private final UserMasterDoa userMasterDoa;

    @GetMapping("/health")
    public ResponseEntity<String> checkActiveStatus(@RequestParam String apiKey) {
        if(checkApiKey(apiKey)) {
            return ResponseEntity.badRequest().body("It's not that easy my friend :D");
        }
        return new ResponseEntity<>("Pocket Buddy is live on - https://www.nexlogix.com" + ConstantsUrls.HOST_URL, HttpStatus.OK);
    }

    @GetMapping("/db")
    public ResponseEntity<String> checkDbStatus(@RequestParam String apiKey) {
        if(checkApiKey(apiKey)) {
            return ResponseEntity.badRequest().body("It's not that easy my friend :D");
        }
        try {
            UserDocument demoUser = UserDocument.builder()
                    .userId(UUID.randomUUID().toString())
                    .userFirstName("Checking")
                    .userLastName("Database")
                    .username("checking_database_status")
                    .email("demo@example.com")
                    .password("pocketbuddy@123")
                    .createdDate(new Date())
                    .lastUpdatedDate(new Date())
                    .build();

            userMasterDoa.save(demoUser);
            UserDocument savedUser = userMasterDoa.findById(demoUser.getUserId()).orElse(null);
            if(ObjectUtils.isEmpty(savedUser)) {
                return ResponseEntity.badRequest().body("No such user found!");
            }
            userMasterDoa.delete(savedUser);
            return ResponseEntity.ok("Database is working fine! User created and deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error checking database: " + e.getMessage());
        }
    }

    private boolean checkApiKey(String apiKey) {
        return !apiKey.equals(ConstantsVariables.API_KEY);
    }


}
