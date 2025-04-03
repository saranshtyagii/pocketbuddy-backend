package com.web.pocketbuddy.controller.app;

import com.web.pocketbuddy.constants.UrlsConstants;
import com.web.pocketbuddy.entity.tracking.ThreadContextUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlsConstants.BASE_URL_V1)
public class HomeController {

    @GetMapping("/native")
    public ResponseEntity<Void> homeNative(@RequestParam boolean isLogIn,
                                        @RequestParam int appVersion,
                                        @RequestParam String userId,
                                        @RequestParam String source,
                                        @RequestParam String ip,
                                        @RequestParam String location) {
        ThreadContextUtils.buildThread(isLogIn, appVersion, userId, source, ip, location);
        return null;
    }

    @GetMapping("/web")
    public ResponseEntity<Void> homeWeb(@RequestParam boolean isLogIn,
                                     @RequestParam int appVersion,
                                     @RequestParam String userId,
                                     @RequestParam String source,
                                     @RequestParam String ip,
                                     @RequestParam String location) {
        ThreadContextUtils.buildThread(isLogIn, appVersion, userId, source, ip, location);
        return null;
    }

    @GetMapping("/ios")
    public ResponseEntity<Void> homeIos(@RequestParam boolean isLogIn,
                                     @RequestParam int appVersion,
                                     @RequestParam String userId,
                                     @RequestParam String source,
                                     @RequestParam String ip,
                                     @RequestParam String location) {
        ThreadContextUtils.buildThread(isLogIn, appVersion, userId, source, ip, location);
        return null;
    }

}

