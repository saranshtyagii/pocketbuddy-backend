package com.web.pocketbuddy.entity.dao;

import com.web.pocketbuddy.entity.document.GroupDocument;
import com.web.pocketbuddy.entity.document.GroupExpenseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupDetailsMasterDoa extends MongoRepository<GroupDocument, String> {

    @Query("{ 'groupName': { $regex: ?0, $options: 'i' } }")
    Optional<List<GroupDocument>> findAllByGroupName(String groupName);
}
