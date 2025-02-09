package com.web.pocketbuddy.controller.user;

import com.web.pocketbuddy.constants.ConstantsUrls;
import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.payload.RegisterUser;
import com.web.pocketbuddy.payload.UserCredentials;
import com.web.pocketbuddy.security.JwtTokenUtils;
import com.web.pocketbuddy.security.JwtUserDetailService;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.service.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ConstantsUrls.AUTH_URL)
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailService userDetailService;
    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody UserCredentials userCredentials) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredentials.getUsernameOrEmail(), userCredentials.getPassword())
        );

        UserDetails userDetails = userDetailService.loadUserByUsername(userCredentials.getUsernameOrEmail());
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUser registerUser) {
        UserDetailResponse response = userService.registerUser(registerUser);
        return ResponseEntity.ok(MapperUtils.convertObjectToString(response));
    }

}
