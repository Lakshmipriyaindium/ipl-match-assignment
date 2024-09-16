package com.indium.spring_boot_assignment;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringBootAssignmentApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(SpringBootAssignmentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
