package com.reorbita.domain.entity;

import com.reorbita.domain.enums.RepairKind;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.Duration;

@Entity
@DiscriminatorValue("MODULE_SWAP")
public class ModuleSwapRobot extends RepairRobot {

    @Column(name = "module_slots")
    private int moduleSlots;

    protected ModuleSwapRobot() {
    }

    public ModuleSwapRobot(String codename, int moduleSlots) {
        super(codename);
        this.moduleSlots = moduleSlots;
    }

    @Override
    public RepairKind specialty() {
        return RepairKind.MODULE_SWAP;
    }

    @Override
    public boolean canHandle(RepairKind kind) {
        return kind == RepairKind.MODULE_SWAP;
    }

    @Override
    public Duration estimateMissionDuration(Satellite target) {
        return Duration.ofHours(8L + target.maintenanceComplexity() * 3L);
    }

    public int getModuleSlots() {
        return moduleSlots;
    }
}
