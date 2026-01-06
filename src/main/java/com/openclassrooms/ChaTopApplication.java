package com.openclassrooms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ChaTopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChaTopApplication.class, args);
	}

}
