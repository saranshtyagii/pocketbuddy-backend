package com.web.pocketbuddy;

import com.web.pocketbuddy.controller.expense.GroupExpenseController;
import com.web.pocketbuddy.service.response.GroupExpenseResponseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PocketBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocketBuddyApplication.class, args);
	}

}
