package com.conference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.conference.config.ApplicationProperties;

/**
 * @author Nagarjuna Paritala
 */

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class ConferenceRoomBookingApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConferenceRoomBookingApplication.class, args);
	}
}
