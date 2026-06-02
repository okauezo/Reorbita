package com.reorbita;

import com.reorbita.application.service.SatelliteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReorbitaApplicationTests {

    @Autowired
    private SatelliteService satelliteService;

    @Test
    void contextLoadsAndSeedsSatellites() {
        assertThat(satelliteService.getAll()).hasSize(2);
    }
}
