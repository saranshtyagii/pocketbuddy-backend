package com.web.pocketbuddy.entity.dao;

import com.web.pocketbuddy.entity.document.PersonalExpenseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PersonalExpenseMasterDao extends MongoRepository<PersonalExpenseDocument, String> {
    Optional<List<PersonalExpenseDocument>> findByUserId(String userId);

    Optional<List<PersonalExpenseDocument>> findAllByUserId(String userId);
}
