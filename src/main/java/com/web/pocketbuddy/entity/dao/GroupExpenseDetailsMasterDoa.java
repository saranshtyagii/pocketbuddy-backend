package com.web.pocketbuddy.entity.dao;

import com.web.pocketbuddy.entity.document.GroupDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupExpenseDetailsMasterDoa extends MongoRepository<GroupDocument, String> {
}
