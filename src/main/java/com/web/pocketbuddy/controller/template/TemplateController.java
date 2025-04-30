package com.web.pocketbuddy.controller.template;

import com.web.pocketbuddy.constants.UrlsConstants;
import com.web.pocketbuddy.exception.UserApiException;
import com.web.pocketbuddy.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/template")
@AllArgsConstructor
public class TemplateController {

    private final UserService userService;

    @GetMapping("/change-password")
    public String getChangePasswordTemplate(@RequestParam String token, Model model) {
        if(StringUtils.isEmpty(token)) {
            throw new UserApiException("Token can't be empty", HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("token", token);
        model.addAttribute("url", UrlsConstants.AUTH_URL + "/update-password");
        return "Update_Password";
    }

    @GetMapping("/email-verification-successfully")
    public String getEmailVerificationSuccessfullyTemplate(@RequestParam String email, Model model) {
        boolean isEmailVerified = userService.isEmailVerified(email);
        if(isEmailVerified) {
            return "Verify_Email";
        }
        return "Something went wrong!";
    }


}
