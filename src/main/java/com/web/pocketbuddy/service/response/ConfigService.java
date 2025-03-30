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
	private static volatile Config config;
	private final UserService userService;
	private final PersonalExpenseService personalExpenseService;
	private final GroupExpenseService groupExpenseService;

	private boolean validationHappens = false;

	@Autowired
	public ConfigService(ConfigMasterDoa configMasterDoa, UserService userService,
						 PersonalExpenseService personalExpenseService, GroupExpenseService groupExpenseService) {
		this.configMasterDoa = configMasterDoa;
		this.userService = userService;
		this.personalExpenseService = personalExpenseService;
		this.groupExpenseService = groupExpenseService;
	}

	@EventListener(ContextRefreshedEvent.class)
    public void loadConfig() {
		try {
			if(!validationHappens) {
				validateResources();
			}
			List<Config> configs = configMasterDoa.findAll();

			if (configs.isEmpty()) {
				System.err.println("Config not found! Creating a new one...");
				setConfig();
			} else {
				config = configs.get(0); // Load the first config
				System.err.println("Config has been loaded successfully.");
			}
		} catch (Exception e) {
			config = null;
			System.err.println("Unable to load config: {\n"+e.getMessage()+"\n}");
		}
	}

	private void validateResources() {
		Thread validationThread = new Thread(() -> {
			try {
				System.err.println("Validating Pocket Buddy MongoDB connections...");

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

				PersonalExpenseResponse savedPersonalExpense = personalExpenseService.addPersonalExpense(
						AddPersonalExpense.builder()
								.userID(savedUser.getUserId())
								.description("Validation Pocket Buddy.")
								.amount(10)
								.build()
				);

				personalExpenseService.deletePersonalExpenseFromDB(ConstantsVariables.API_KEY, savedPersonalExpense.getExpenseId());
				userService.deleteUserFromDb(ConstantsVariables.API_KEY, savedUser.getUserId());

				System.err.println("Validation successful!");
				validationHappens = true;
			} catch (Exception e) {
				System.err.println("Validation failed: {}");
			}
		});

		validationThread.start();
		try {
			validationThread.join(5000); // Prevent indefinite waiting
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Resource validation interrupted", e);
		}
	}

	public static Config getInstance() {
		return ObjectUtils.isEmpty(config) ? null : config;
	}

	private void setConfig() {
		System.err.println("Creating new default config...");

		Config newConfig = Config.builder()
				.crmEnabled(false)
				.crmAccessToken("REPLACE_WITH_SECURE_VALUE") // Avoid hardcoded secrets
				.jwtSecretKey("REPLACE_WITH_SECURE_VALUE")
				.refreshApplicationEnable(false)
				.build();

		configMasterDoa.save(newConfig);
		config = newConfig;
	}
}
