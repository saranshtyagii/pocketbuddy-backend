package com.web.pocketbuddy.entity.dao;

import com.web.pocketbuddy.entity.document.GroupDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GroupDetailsMasterDao extends MongoRepository<GroupDocument, String> {
    Optional<GroupDocument> findByGroupId(String groupId);
}
