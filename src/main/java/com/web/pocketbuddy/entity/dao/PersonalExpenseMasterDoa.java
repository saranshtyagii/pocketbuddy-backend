package com.web.pocketbuddy.entity.dao;

import com.web.pocketbuddy.entity.document.PersonalExpenseDocument;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PersonalExpenseMasterDoa extends MongoRepository<PersonalExpenseDocument, String> {

    Optional<PersonalExpenseDocument> findByUserId(@NotNull(message = "userId can't be null") String userId);

    Optional<List<PersonalExpenseDocument>> findAllByUserId(@NotNull(message = "userId can't be null") String userId);
}
