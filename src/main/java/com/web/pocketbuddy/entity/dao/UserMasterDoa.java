package com.web.pocketbuddy.entity.dao;

import com.web.pocketbuddy.entity.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserMasterDoa extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByEmailOrUsername(String email, String username);
    Optional<UserDocument> findByUsername(String username);
    Optional<UserDocument> findByEmail(String email);

    Optional<UserDocument> findByMobileNumber(String mobileNumber);
}
