package com.conference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.conference.config.ApplicationProperties;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Nagarjuna Paritala
 */

@SpringBootApplication
@EnableSwagger2	
@EnableConfigurationProperties({ApplicationProperties.class})
public class ConferenceRoomBookingApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConferenceRoomBookingApplication.class, args);
	}
}
