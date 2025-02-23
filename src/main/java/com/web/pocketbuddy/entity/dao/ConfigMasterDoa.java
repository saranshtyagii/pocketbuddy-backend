package com.web.pocketbuddy.entity.dao;

import com.web.pocketbuddy.entity.document.Config;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigMasterDoa extends MongoRepository<Config, String> {
}
