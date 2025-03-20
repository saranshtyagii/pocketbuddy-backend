package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.entity.dao.ConfigMasterDoa;
import com.web.pocketbuddy.entity.document.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class ConfigService {

	private final ConfigMasterDoa configMasterDoa;
	private static Config config;

	@Autowired
	public ConfigService(ConfigMasterDoa configMasterDoa) {
		this.configMasterDoa = configMasterDoa;
	}

	@EventListener(ContextRefreshedEvent.class)
	private void loadConfig() {
		try {
			List<Config> configs = configMasterDoa.findAll();
			if(configs.isEmpty()) {
				System.err.println("Config not found!");
				setConfig();
			}
			System.err.println(configs.get(0).toString());
			if (!configs.isEmpty()) {
				config = configs.get(0); // Load the first config
				System.err.println("config has been loaded successfully:");
			}
		} catch (Exception e) {
			config = null;
			System.err.println("Unable to load config: " + e.getMessage());
		}
	}

	public static Config getConfig() {
		if(config == null) {
			throw new RuntimeException("Config not found!");
		}
		return config;
	}

	public static Config getInstance() {
		try {
			if (ObjectUtils.isEmpty(config)) {
				throw new RuntimeException("Unable to fetch config from the server");
			}
			return config;
		} catch (Exception e) {

			return null;
		}
	}

	public void setConfig() {
		System.err.println("Creating new config... Please wait...");
		configMasterDoa.save(new Config(true, "", ""));
	}

}
