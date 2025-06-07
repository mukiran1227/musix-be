package com.gig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.gig"})
@EnableAutoConfiguration(exclude={UserDetailsServiceAutoConfiguration.class})
public class GigApplication {

	public static void main(String[] args) {
		SpringApplication.run(GigApplication.class, args);
	}

}
