package com.reorbita.config;

import com.reorbita.domain.entity.DebrisCaptureRobot;
import com.reorbita.domain.entity.LegacySatellite;
import com.reorbita.domain.entity.ModuleSwapRobot;
import com.reorbita.domain.entity.MotherStation;
import com.reorbita.domain.entity.OrbitReadySatellite;
import com.reorbita.domain.entity.RefuelRobot;
import com.reorbita.domain.entity.Satellite;
import com.reorbita.domain.entity.Sensor;
import com.reorbita.domain.entity.TelemetryReading;
import com.reorbita.domain.entity.TrajectoryRobot;
import com.reorbita.domain.enums.SensorType;
import com.reorbita.domain.vo.BatteryHealth;
import com.reorbita.domain.vo.OrbitalPosition;
import com.reorbita.repository.MotherStationRepository;
import com.reorbita.repository.SatelliteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SatelliteRepository satelliteRepository;
    private final MotherStationRepository stationRepository;

    public DataInitializer(SatelliteRepository satelliteRepository, MotherStationRepository stationRepository) {
        this.satelliteRepository = satelliteRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (satelliteRepository.count() > 0) {
            return;
        }

        LocalDateTime reference = LocalDateTime.now();

        MotherStation station = new MotherStation("Estacao-Mae Orion", 520);
        station.dockRobot(new RefuelRobot("RB-FUEL-01", 140));
        station.dockRobot(new ModuleSwapRobot("RB-SWAP-01", 4));
        station.dockRobot(new TrajectoryRobot("RB-TRAJ-01", 450));
        station.dockRobot(new DebrisCaptureRobot("RB-NET-01", 14));
        stationRepository.save(station);

        OrbitReadySatellite aurora = new OrbitReadySatellite(
                "Aurora-1", "Helios Space", "55001",
                reference.minusYears(3),
                new OrbitalPosition(540, 53.2, 7.6),
                new BatteryHealth(92, 1200, reference.minusDays(60)),
                "ORB-C 1.2");
        aurora.registerSensor(new Sensor(SensorType.BATTERY, "%"));
        aurora.registerSensor(new Sensor(SensorType.SOLAR_PANEL, "W"));
        aurora.registerSensor(new Sensor(SensorType.ORBIT_TRACKER, "km"));
        seedTelemetry(aurora, reference, 92, 0.9, 70);

        LegacySatellite vetus = new LegacySatellite(
                "Vetus-7", "Antares Telecom", "41888",
                reference.minusYears(9),
                new OrbitalPosition(780, 98.1, 7.4),
                new BatteryHealth(64, 4200, reference.minusDays(60)));
        vetus.registerSensor(new Sensor(SensorType.BATTERY, "%"));
        vetus.registerSensor(new Sensor(SensorType.GYROSCOPE, "deg/s"));
        seedTelemetry(vetus, reference, 64, 0.15, 22);

        satelliteRepository.save(aurora);
        satelliteRepository.save(vetus);
    }

    private void seedTelemetry(Satellite satellite, LocalDateTime reference, double startBattery,
                               double batteryDropPerDay, double startFuel) {
        for (int day = 60; day >= 0; day -= 5) {
            LocalDateTime timestamp = reference.minusDays(day);
            int elapsed = 60 - day;
            double battery = clamp(startBattery - batteryDropPerDay * elapsed);
            double fuel = clamp(startFuel - 0.1 * elapsed);
            double solar = 1400 - elapsed * 4.0;
            double deviation = 0.5 + elapsed * 0.05;

            satellite.pushTelemetry(new TelemetryReading(
                    timestamp, battery, fuel, solar, deviation, -4 + elapsed * 0.1));
        }
    }

    private double clamp(double value) {
        return Math.max(0, Math.min(100, value));
    }
}
