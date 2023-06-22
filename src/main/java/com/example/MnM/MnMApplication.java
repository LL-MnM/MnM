package com.example.MnM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class MnMApplication {

	public static void main(String[] args) {
		SpringApplication.run(MnMApplication.class, args);
	}

}
