package com.web.pocketbuddy.service;

import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.payload.RegisterUser;

public interface UserService {

    public UserDetailResponse registerUser(RegisterUser registerUser);
    public UserDetailResponse findUserByUsername(String username);
    public UserDetailResponse findUserByEmail(String email);
    public UserDetailResponse findUserByPhone(String phone);
    public UserDetailResponse findUserById(String id);


}
