package com.web.pocketbuddy.security;

import com.web.pocketbuddy.constants.ConstantsVariables;
import com.web.pocketbuddy.entity.dao.UserMasterDao;
import com.web.pocketbuddy.entity.document.UserDocument;
import com.web.pocketbuddy.exception.UserApiException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private final UserMasterDao userMasterDoa;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDocument savedUser = userMasterDoa.findByEmail(email)
                .orElseThrow(() -> new UserApiException(ConstantsVariables.NO_SUCH_USER_FOUND, HttpStatus.BAD_REQUEST));

        return new User(
                savedUser.getEmail(),
                savedUser.getPassword(),
                Collections.emptyList()
        );
    }
}
