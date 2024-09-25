package com.onepage.coupong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CoupongApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoupongApplication.class, args);
	}

}
