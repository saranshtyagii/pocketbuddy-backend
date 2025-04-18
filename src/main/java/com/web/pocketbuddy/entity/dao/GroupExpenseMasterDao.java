package com.web.pocketbuddy.entity.dao;

import com.web.pocketbuddy.entity.document.GroupExpenseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GroupExpenseMasterDao extends MongoRepository<GroupExpenseDocument, String> {

    List<GroupExpenseDocument> findByGroupId(String groupId);

    void deleteByGroupId(String groupId);

    Optional<GroupExpenseDocument> findByExpenseId(String expenseId);
}
