package com.web.pocketbuddy.service.response;

import com.web.pocketbuddy.constants.ConstantsVariables;
import com.web.pocketbuddy.dto.PersonalExpenseResponse;
import com.web.pocketbuddy.dto.UserDetailResponse;
import com.web.pocketbuddy.entity.dao.ConfigMasterDoa;
import com.web.pocketbuddy.entity.document.Config;
import com.web.pocketbuddy.payload.AddPersonalExpense;
import com.web.pocketbuddy.payload.RegisterUser;
import com.web.pocketbuddy.service.GroupExpenseService;
import com.web.pocketbuddy.service.PersonalExpenseService;
import com.web.pocketbuddy.service.UserService;
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
	private final UserService userService;
	private final PersonalExpenseService personalExpenseService;
	private final GroupExpenseService groupExpenseService;

	@Autowired
	public ConfigService(ConfigMasterDoa configMasterDoa, UserService userService, PersonalExpenseService personalExpenseService, GroupExpenseService groupExpenseService) {
		this.configMasterDoa = configMasterDoa;
		this.userService = userService;
		this.personalExpenseService = personalExpenseService;
		this.groupExpenseService = groupExpenseService;
	}

	@EventListener(ContextRefreshedEvent.class)
	private void loadConfig() {
		try {
			validateResources();
			List<Config> configs = configMasterDoa.findAll();
			if(configs.isEmpty()) {
				System.err.println("Config not found!");
				setConfig();
			}
			if (!configs.isEmpty()) {
				config = configs.get(0); // Load the first config
				System.err.println("config has been loaded successfully:");
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
									.mobileNumber("7011097691")
									.password("pocketbuddy@123")
									.build()
					);

					PersonalExpenseResponse savedPersonalExpenseResponse = personalExpenseService.addPersonalExpense(
							AddPersonalExpense.builder()
									.userID(savedUser.getUserId())
									.description("Validation Pocket Buddy.")
									.amount(10)
									.build()
					);

					personalExpenseService.deletePersonalExpenseFromDB(ConstantsVariables.API_KEY ,savedPersonalExpenseResponse.getExpenseId());
					userService.deleteUserFromDb(ConstantsVariables.API_KEY, savedUser.getUserId());

					System.err.println("Validation successfully!");

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
		configMasterDoa.save(Config.builder().build());
	}

}
