package com.web.pocketbuddy;

import com.web.pocketbuddy.constants.ConstantsVariables;
import com.web.pocketbuddy.service.GroupExpenseService;
import com.web.pocketbuddy.service.PersonalExpenseService;
import com.web.pocketbuddy.service.UserService;
import com.web.pocketbuddy.service.response.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@AllArgsConstructor
public class PocketBuddyApplication {

	private final UserService userService;
	private final PersonalExpenseService personalExpenseService;
	private final GroupExpenseService groupExpenseService;
	private final ConfigService configService;

	public static void main(String[] args) {
		SpringApplication.run(PocketBuddyApplication.class, args);
	}

	@Scheduled(cron = "0 */30 * * * ?")
	public void refershApplicationChache() {
		configService.loadConfig();
		System.err.println("Refershing application chache: "+ ConfigService.getInstance().isRefreshApplicationEnable());
		if(ConfigService.getInstance().isRefreshApplicationEnable()) {
			groupExpenseService.deleteGroupFromDb(ConstantsVariables.API_KEY);
			personalExpenseService.deletePersonalExpense(ConstantsVariables.API_KEY);
			userService.deleteUserFromDb(ConstantsVariables.API_KEY, null);
		}
	}


}