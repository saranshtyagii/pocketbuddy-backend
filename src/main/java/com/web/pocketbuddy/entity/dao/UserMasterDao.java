package com.web.pocketbuddy.entity.dao;

import com.web.pocketbuddy.entity.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserMasterDao extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByEmail(String email);

    Optional<UserDocument> findByMobileNumber(String mobileNumber);

    Optional<UserDocument> findByEmailVerificationToken(String token);

    Optional<UserDocument> findByChangePasswordToken(String token);
}
