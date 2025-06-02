package com.web.pocketbuddy.TextController;

import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.payload.RegisterGroupExpense;
import com.web.pocketbuddy.payload.RegisterUser;
import com.web.pocketbuddy.service.GroupDetailsService;
import com.web.pocketbuddy.service.GroupExpenseService;
import com.web.pocketbuddy.service.PersonalExpenseService;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.utils.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController()
@RequestMapping("/test/group")
@AllArgsConstructor
public class GroupTestController {

    private final GroupExpenseService groupExpenseService;
    private final GroupDetailsService groupDetailsService;
    private final UserService userService;
    private final PersonalExpenseService personalExpenseService;

    @RequestMapping("/fetchOweAmount")
    public ResponseEntity<Map<String, Map<String, Double>>> fetchOweAmount(String groupId) {
        return ResponseEntity.ok(groupExpenseService.fetchOweAmount(groupId));
    }

    @RequestMapping("/createDummyData")
    public ResponseEntity<String> createDummyData() {
        try {
            // create 4 demo users
            List<RegisterUser> users = createDummyUsers();
            users.forEach(user -> userService.registerUser(user));

            // mark email as verified
            List<UserDocument> savedUsers = new ArrayList<>();
            users.forEach(user -> {
                UserDocument savedUser = userService.findUserByEmailAsDocument(user.getEmail(), ConfigService.getInstance().getApiKey());
                savedUser.setEmailVerified(true);
                userService.savedUpdatedUser(savedUser);
                savedUsers.add(savedUser);
            });

            // create Group
            GroupDetailsResponse savedGroup = groupDetailsService.registerGroup(GroupRegisterDetails.builder()
                    .createdByUser(savedUsers.get(0).getUserId())
                    .description("Demo Group for Testing the flow")
                    .build());

            // join group By User
            for(int i=1;i<savedUsers.size();i++) {
                groupDetailsService.joinGroup(savedGroup.getGroupId(), savedUsers.get(i).getUserId());
            }

            // create dummy group expense
            savedUsers.forEach(user -> {
                Double amount = (double) ThreadLocalRandom.current().nextInt(1000, 10000);
                RegisterGroupExpense expense = RegisterGroupExpense.builder()
                        .groupId(savedGroup.getGroupId())
                        .description("Dummy Expense")
                        .amount(amount)
                        .includedMembers(Map.of(user.getUserId(), amount))
                        .build();
                groupExpenseService.addExpense(expense);
            });

            return ResponseEntity.ok("Dummy data created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Unable to create dummy data: " + e.getMessage());
        }
    }

    private List<RegisterUser> createDummyUsers() {
        RegisterUser user1 = RegisterUser.builder()
                .userFirstName("Saransh")
                .userLastName("Tyagi")
                .email("saranshtyagi90@gmail.com")
                .password("PASSWORD@1")
                .build();
        RegisterUser user2 = RegisterUser.builder()
                .userFirstName("PC")
                .userLastName("Baqngur")
                .email("work.saranshtyagi@gmail.com")
                .password("PASSWORD@1")
                .build();
        RegisterUser user3 = RegisterUser.builder()
                .userFirstName("Pallav")
                .userLastName("Tyagi")
                .email("tyagisaransh90@gmail.com")
                .password("PASSWORD@1")
                .build();
        RegisterUser user4 = RegisterUser.builder()
                .userFirstName("Csips")
                .userLastName("BGMI")
                .email("tyagisaransh90hpr@gmail.com")
                .password("PASSWORD@1")
                .build();
        return Arrays.asList(user1, user2, user3, user4);
    }

}
