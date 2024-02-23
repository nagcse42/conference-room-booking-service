package com.conference.service;

import com.conference.config.ApplicationProperties;
import com.conference.config.MaintenancePeriodConfig;
import com.conference.service.impl.UtilsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author Nagarjuna Paritala
 */
@ExtendWith(MockitoExtension.class)
class UtilsServiceTest {
    @InjectMocks
    private UtilsService utilsService;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void test_isUnderMaintenanceScheduledTime_true() {
        Mockito.when(applicationProperties.getMaintenancePeriods())
                .thenReturn(mockMaintenancePeriods());
        Assertions.assertTrue(utilsService.isMaintenanceScheduled(LocalTime.of(10, 0),
                LocalTime.of(10, 30)));
    }

    @Test
    void test_isUnderMaintenanceScheduledTime_false() {
        Mockito.when(applicationProperties.getMaintenancePeriods())
                .thenReturn(mockMaintenancePeriods());
        Assertions.assertFalse(utilsService.isMaintenanceScheduled(LocalTime.of(12, 0),
                LocalTime.of(13, 0)));
    }

    List<MaintenancePeriodConfig> mockMaintenancePeriods(){
        MaintenancePeriodConfig maintenancePeriodConfig = new MaintenancePeriodConfig();
        maintenancePeriodConfig.setStartTime(LocalTime.of(10, 0));
        maintenancePeriodConfig.setEndTime(LocalTime.of(10, 30));
        return Arrays.asList(maintenancePeriodConfig);
    }
}
