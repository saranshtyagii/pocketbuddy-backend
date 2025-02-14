package com.web.pocketbuddy.controller.add;

import com.web.pocketbuddy.constants.ConstantsUrls;
import com.web.pocketbuddy.dto.AddResponseDto;
import com.web.pocketbuddy.service.response.AddServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ConstantsUrls.ADD_URL)
public class AddController {

    public ResponseEntity<AddResponseDto> fetchAddUrl(@RequestParam String currentScreen, @RequestParam String screenPlacement) {
        return ResponseEntity.ok(AddServiceResponse.fetchAddForUser(currentScreen, screenPlacement));
    }

}
