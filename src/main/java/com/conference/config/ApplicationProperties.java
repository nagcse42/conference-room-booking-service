package com.conference.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author Nagarjuna Paritala
 */
@Data
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
	private List<MaintenancePeriodConfig> maintenancePeriods;
}
