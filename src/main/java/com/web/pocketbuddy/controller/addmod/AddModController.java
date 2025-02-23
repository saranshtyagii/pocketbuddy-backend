package com.web.pocketbuddy.controller.addmod;

import com.web.pocketbuddy.constants.ConstantsUrls;
import com.web.pocketbuddy.dto.AddResponseDto;
import com.web.pocketbuddy.service.response.AddServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ConstantsUrls.ADD_MOD_URL)
public class AddModController {

    public ResponseEntity<AddResponseDto> fetchAddUrl(@RequestParam String currentScreen, @RequestParam String screenPlacement) {
        return ResponseEntity.ok(AddServiceResponse.fetchAddForUser(currentScreen, screenPlacement));
    }

}
