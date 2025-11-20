package net.sodiumzh.nff.girls.gaia.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import javax.annotation.Nullable;

public interface INFFGirlsGaiaChargeAttackingMob {

    public boolean isCharging();

    public void setIsCharging(boolean charging);

    public void playChargeAttackSound();
}

