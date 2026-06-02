package com.reorbita.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class OrbitalPosition {

    @Column(name = "altitude_km")
    private double altitudeKm;

    @Column(name = "inclination_deg")
    private double inclinationDeg;

    @Column(name = "velocity_km_s")
    private double velocityKmS;

    protected OrbitalPosition() {
    }

    public OrbitalPosition(double altitudeKm, double inclinationDeg, double velocityKmS) {
        if (altitudeKm <= 0) {
            throw new IllegalArgumentException("A altitude orbital deve ser positiva.");
        }
        if (inclinationDeg < 0 || inclinationDeg > 180) {
            throw new IllegalArgumentException("A inclinacao deve estar entre 0 e 180 graus.");
        }
        if (velocityKmS <= 0) {
            throw new IllegalArgumentException("A velocidade orbital deve ser positiva.");
        }
        this.altitudeKm = altitudeKm;
        this.inclinationDeg = inclinationDeg;
        this.velocityKmS = velocityKmS;
    }

    public double getAltitudeKm() {
        return altitudeKm;
    }

    public double getInclinationDeg() {
        return inclinationDeg;
    }

    public double getVelocityKmS() {
        return velocityKmS;
    }

    public boolean isLowEarthOrbit() {
        return altitudeKm <= 2000;
    }

    public double deviationFrom(OrbitalPosition reference) {
        return Math.abs(altitudeKm - reference.altitudeKm);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrbitalPosition other)) {
            return false;
        }
        return Double.compare(altitudeKm, other.altitudeKm) == 0
                && Double.compare(inclinationDeg, other.inclinationDeg) == 0
                && Double.compare(velocityKmS, other.velocityKmS) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(altitudeKm, inclinationDeg, velocityKmS);
    }
}
