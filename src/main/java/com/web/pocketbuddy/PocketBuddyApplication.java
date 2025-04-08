package com.web.pocketbuddy;

import com.web.pocketbuddy.utils.GenerateUtils;
import com.web.pocketbuddy.utils.RedisUtils;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@AllArgsConstructor
public class PocketBuddyApplication {

	private final RedisUtils redisUtils;

	public static void main(String[] args) {
		SpringApplication.run(PocketBuddyApplication.class, args);
	}

	@PostConstruct
	public void init() {
		GenerateUtils.initialize(redisUtils);
	}

}