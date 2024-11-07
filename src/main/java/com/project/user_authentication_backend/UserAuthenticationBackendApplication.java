package com.project.user_authentication_backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.project.user_authentication_backend.dao")
@EntityScan("com.project.user_authentication_backend.entity")
public class UserAuthenticationBackendApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("SALT_SECRET", dotenv.get("SALT_SECRET"));
		System.setProperty("MAIN_EMAIL", dotenv.get("MAIN_EMAIL"));
		System.setProperty("MAIN_PASSWORD", dotenv.get("MAIN_PASSWORD"));
		SpringApplication.run(UserAuthenticationBackendApplication.class, args);
	}

}