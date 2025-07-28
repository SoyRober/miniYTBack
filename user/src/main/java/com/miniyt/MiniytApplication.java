package com.miniyt;

import com.miniyt.component.TokenProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TokenProperties.class)
public class MiniytApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniytApplication.class, args);
	}

}
