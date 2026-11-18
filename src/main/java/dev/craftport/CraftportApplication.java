package dev.craftport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class CraftportApplication {

	public static void main(String[] args) {
		SpringApplication.run(CraftportApplication.class, args);
	}

}
