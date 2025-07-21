package com.miniyt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MiniytApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniytApplication.class, args);
	}

}
