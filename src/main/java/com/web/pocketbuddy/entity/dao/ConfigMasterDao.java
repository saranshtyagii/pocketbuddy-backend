package com.web.pocketbuddy.entity.dao;

import com.web.pocketbuddy.entity.document.Config;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigMasterDao extends MongoRepository<Config, String> {
}
