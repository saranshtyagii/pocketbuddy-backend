package com.web.pocketbuddy.controller.crm;

import com.web.pocketbuddy.constants.UrlsConstants;
import com.web.pocketbuddy.payload.CrmLoginCred;
import com.web.pocketbuddy.utils.ConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(UrlsConstants.CRM_URL)
public class CrmController {

    @GetMapping("/fetch")
    public String fetchCrm(@RequestBody CrmLoginCred cred) {
        if(!ConfigService.getInstance().isCrmEnabled()) {
            return "CRM Service is not available at that moment please try again later, or contact your administrator for more details.";
        }

        if(!ConfigService.getInstance().getCrmAccessToken().equals(cred.getToken())) {
            return "Access token is incorrect, please try again later";
        }

        return "CRM is on the way .... Please wait .... THANKYOU :)";
    }

}
