package com.web.pocketbuddy.controller.template;

import com.web.pocketbuddy.constants.UrlsConstants;
import com.web.pocketbuddy.exception.UserApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/template")
public class TemplateController {

    @GetMapping("/change-password")
    public String getChangePasswordTemplate(@RequestParam String token, Model model) {
        if(StringUtils.isEmpty(token)) {
            throw new UserApiException("Token can't be empty", HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("token", token);
        model.addAttribute("url", UrlsConstants.AUTH_URL + "/update-password");
        return "Update_Password";
    }


}
