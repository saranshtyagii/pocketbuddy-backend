package com.web.pocketbuddy;

import com.web.pocketbuddy.controller.expense.GroupExpenseController;
import com.web.pocketbuddy.entity.dao.ConfigMasterDoa;
import com.web.pocketbuddy.entity.document.Config;
import com.web.pocketbuddy.service.response.GroupExpenseResponseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@SpringBootApplication
public class PocketBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocketBuddyApplication.class, args);
	}
}