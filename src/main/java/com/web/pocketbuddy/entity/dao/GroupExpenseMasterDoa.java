package com.web.pocketbuddy.entity.dao;

import com.web.pocketbuddy.entity.document.GroupExpenseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GroupExpenseMasterDoa extends MongoRepository<GroupExpenseDocument, String> {
    Optional<List<GroupExpenseDocument>> findByGroupId(String groupId);

    void deleteByGroupId(String groupId);
}
