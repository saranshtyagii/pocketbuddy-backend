package com.web.pocketbuddy.utils;

import com.web.pocketbuddy.constants.ConstantsVariables;
import com.web.pocketbuddy.dto.GroupDetailsResponse;
import com.web.pocketbuddy.dto.GroupExpenseDto;
import com.web.pocketbuddy.dto.PersonalExpenseResponse;
import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.entity.config.ServerConfig;
import com.web.pocketbuddy.entity.dao.ConfigMasterDoa;
import com.web.pocketbuddy.entity.document.Config;
import com.web.pocketbuddy.entity.helper.GroupExpenseMetaData;
import com.web.pocketbuddy.payload.AddPersonalExpense;
import com.web.pocketbuddy.payload.GroupRegisterDetails;
import com.web.pocketbuddy.payload.RegisterGroupExpense;
import com.web.pocketbuddy.payload.RegisterUser;
import com.web.pocketbuddy.service.GroupDetailsService;
import com.web.pocketbuddy.service.GroupExpenseService;
import com.web.pocketbuddy.service.PersonalExpenseService;
import com.web.pocketbuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigService {

	private final ConfigMasterDoa configMasterDoa;
	private static Config config;
	private final UserService userService;
	private final PersonalExpenseService personalExpenseService;
	private final GroupDetailsService groupDetailService;
	private final GroupExpenseService groupExpenseService;
	private final RedisUtils redisUtils;

	@Autowired
	public ConfigService(ConfigMasterDoa configMasterDoa, UserService userService,
						 PersonalExpenseService personalExpenseService,
						 GroupDetailsService groupDetailService,
						 RedisUtils redisUtils, GroupExpenseService groupExpenseService) {
		this.configMasterDoa = configMasterDoa;
		this.userService = userService;
		this.personalExpenseService = personalExpenseService;
        this.groupDetailService = groupDetailService;
        this.groupExpenseService = groupExpenseService;
        this.redisUtils = redisUtils;
    }

	@EventListener(ContextRefreshedEvent.class)
	private void loadConfig() {
		try {
			List<Config> configs = configMasterDoa.findAll();
			if(configs.isEmpty()) {
				System.err.println("Config not found!");
				setConfig();
				loadConfig();
			}
			if (!configs.isEmpty()) {
				config = configs.get(0); // Load the first config
				System.err.println("config has been loaded successfully:");
				if(ConfigService.config.isValidateConnection()) {
					validateResources();
				}
			}
		} catch (Exception e) {
			config = null;
			System.err.println("Unable to load config: " + e.getMessage());
		}
	}

	private void validateResources() {
		try {
			Thread checkMongoConnections = new Thread(() -> {
				try {
					System.err.println("Validating Pocket Buddy Mongo connections...");

					UserDetailResponse savedUser = userService.registerUser(
							RegisterUser.builder()
									.userFirstName("Pocket")
									.userLastName("Buddy")
									.username("pocketbuddy")
									.email("contact@pocketbuddy.app")
									.mobileNumber("0000000000")
									.password("pocketbuddy@123")
									.build()
					);

					PersonalExpenseResponse savedPersonalExpenseResponse = personalExpenseService.addPersonalExpense(
							AddPersonalExpense.builder()
									.userId(savedUser.getUserId())
									.description("Validation Pocket Buddy.")
									.amount(10)
									.build()
					);

					GroupDetailsResponse savedGroup = groupDetailService.registerGroup(GroupRegisterDetails.builder()
									.groupName("Pocket Buddy")
									.description("Validation Pocket Buddy")
									.createdByUser(savedUser.getUserId())
							.build());

					Map<String, GroupExpenseMetaData> includedMembers = new HashMap<>();
					includedMembers.put(savedUser.getUserId(), new GroupExpenseMetaData(savedUser.getUserFirstName(), 10.0));

					GroupExpenseDto savedGroupExpense = groupExpenseService.addExpense(
							RegisterGroupExpense.builder()
									.groupId(savedGroup.getGroupId())
									.description("Validation Pocket Buddy.")
									.amount(10)
									.includedMembers(includedMembers)
									.build()
					);

					groupExpenseService.deleteExpense(savedGroupExpense.getGroupId(), ConfigService.getConfig().getApiKey());
					groupDetailService.deleteGroup(savedGroup.getGroupId(), savedUser.getUserId());
					personalExpenseService.deletePersonalExpenseFromDb(ConfigService.getConfig().getApiKey(), savedPersonalExpenseResponse.getExpenseId());
					userService.deleteUserFromDb(savedUser.getUserId());

					System.err.println("Mongo Validation successfully!");

					System.err.println("\nValidating Pocket Buddy Redis connections...");
					String key = "ValidatingPocketBuddyRedisConnections";
					String value = "Pocket Buddy Redis connections";
					redisUtils.set(key, value);
					redisUtils.del(key);

					System.err.println("Redis Validation successfully!\n");

				} catch (Exception e) {
					System.err.println("Validation failed: " + e.getMessage());
					e.printStackTrace();
				}
			});

			checkMongoConnections.start();
			checkMongoConnections.join();

		} catch (Exception e) {
			throw new RuntimeException("Failed to validate resources", e);
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
		configMasterDoa.save(
				Config.builder()
						.crmEnabled(false)
						.crmAccessToken("")
						.jwtSecretKey("")
						.apiKey("")
						.adminPassword("")
						.validateConnection(true)
				.build());
	}

	public void refreshConfig() {
		loadConfig();
	}
}
