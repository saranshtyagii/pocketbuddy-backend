package com.web.pocketbuddy.controller.validate;

import com.web.pocketbuddy.constants.UrlsConstants;
import com.web.pocketbuddy.entity.dao.GroupDetailsMasterDao;
import com.web.pocketbuddy.entity.dao.GroupExpenseMasterDao;
import com.web.pocketbuddy.entity.dao.PersonalExpenseMasterDao;
import com.web.pocketbuddy.entity.dao.UserMasterDao;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.exception.UserApiException;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import com.web.pocketbuddy.utils.ConfigService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController()
@AllArgsConstructor
@RequestMapping(UrlsConstants.BASE_URL_V1 + "/status")
public class ApiStatusController {

    private final UserMasterDao userMasterDoa;
    private final GroupDetailsMasterDao groupDetailsMasterDao;
    private final GroupExpenseMasterDao groupExpenseMasterDao;
    private final PersonalExpenseMasterDao personalExpenseMasterDao;
    private final ConfigService configService;

    @GetMapping("/health")
    public ResponseEntity<String> checkActiveStatus(@RequestParam String apiKey) {
        if(checkApiKey(apiKey)) {
            return ResponseEntity.badRequest().body("It's not that easy my friend :D:");
        }
        return new ResponseEntity<>("Pocket Buddy is live on - https://www.nexlogix.com" + UrlsConstants.HOST_URL, HttpStatus.OK);
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
            return ResponseEntity.internalServerError().body("Database is not working fine, please check!");
        }
    }

    @GetMapping("/fetchdb")
    public ResponseEntity<List<?>> fetchDatabase(@RequestParam String apiKey, @RequestParam String password, String collectionName) {
        if(checkApiKey(apiKey) || checkAdminPassword(password)) {
            throw new UserApiException("It's not that easy my friend :D", HttpStatus.FORBIDDEN);
        }

        switch (collectionName) {
            case "users":
                return new ResponseEntity<>(userMasterDoa.findAll(), HttpStatus.OK);
            case "groups":
                return new ResponseEntity<>(groupDetailsMasterDao.findAll(), HttpStatus.OK);
            case "group-expenses":
                return new ResponseEntity<>(groupExpenseMasterDao.findAll(), HttpStatus.OK);
            case "personal-expenses":
                return new ResponseEntity<>(personalExpenseMasterDao.findAll(), HttpStatus.OK);
            default:
                throw new UserApiException("No such collection found!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/remove-id")
    public ResponseEntity<String> removeId(@RequestParam String apiKey, @RequestParam String password, @RequestParam String userId) {
        if(checkApiKey(apiKey) || checkAdminPassword(password)) {
            return ResponseEntity.badRequest().body("It's not that easy my friend :D");
        }
        UserDocument userDocument = userMasterDoa.findById(userId).orElse(null);
        if(ObjectUtils.isEmpty(userDocument)) {
            return ResponseEntity.badRequest().body("No such user found!");
        }
        userMasterDoa.delete(userDocument);
        return ResponseEntity.ok("User Account with id: " +userId+ " deleted successfully.");
    }

    @GetMapping("/config")
    private ResponseEntity<String> fetchServerConfig(@RequestParam String apiKey, @RequestParam String password) {
        System.err.println("Config api call");
        if(checkApiKey(apiKey) || checkAdminPassword(password)) {
            return ResponseEntity.badRequest().body("It's not that easy my friend :D");
        }

        return new ResponseEntity<>(MapperUtils.convertObjectToString(ConfigService.getInstance()), HttpStatus.OK);

    }

    @GetMapping("/refresh-config")
    private ResponseEntity<String> fetchRefreshConfig(@RequestParam String apiKey, @RequestParam String adminPassword) {

        if(StringUtils.isBlank(ConfigService.getConfig().getApiKey()) && StringUtils.isBlank(ConfigService.getConfig().getAdminPassword())) {
            configService.refreshConfig();
        }

        if(checkApiKey(apiKey) && !adminPassword.equals(ConfigService.getConfig().getAdminPassword())) {
            throw new UserApiException("It's not that easy my friend :D", HttpStatus.BAD_REQUEST);
        }

        configService.refreshConfig();
        return ResponseEntity.ok("Config refreshed successfully.");
    }

    private boolean checkApiKey(String apiKey) {
        return !apiKey.equals(ConfigService.getConfig().getApiKey());
    }

    private boolean checkAdminPassword(String password) {
        return !password.equals(ConfigService.getConfig().getAdminPassword());
    }

}
