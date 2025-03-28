package com.web.pocketbuddy.controller.helperController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/static")
public class StaticFileController {

    @GetMapping("/reset-password")
    public String resetPassword(String token) {
        return "UpdatePassword";
    }

}
