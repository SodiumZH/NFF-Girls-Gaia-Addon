package net.sodiumzh.nff.girls.gaia.entity.ai;

import gaia.entity.Banshee;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.girls.entity.ai.NFFGirlsAIUtils;
import net.sodiumzh.nff.girls.gaia.entity.INFFGirlsGaiaChargeAttackingMob;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.ai.goal.NFFGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nfu.math.RangedRandomDouble;
import net.sodiumzh.nfu.util.NFUMathStatics;

import java.util.EnumSet;

public class NFFGirlsGaiaFlyingChargeAttackGoal extends NFFGoal {

    protected final INFFGirlsGaiaChargeAttackingMob chargeAttackingMob;
    protected double speedModifier;
    protected double maxStartingDistance = 9.0d;
    protected double minStartingDistance = 1.0d;
    protected double stoppingDistance = 18d;
    protected RangedRandomDouble withdrawalDistanceRange = RangedRandomDouble.uniform(7d, 9d);
    protected boolean isApproaching = false; // True = approaching the target; false = leaving for next attack
    protected int attackInterval = 30;
    protected int attackTimer = 0;

    public NFFGirlsGaiaFlyingChargeAttackGoal(INFFTamed tamed, double speedModifier) {
        super(tamed);
        this.chargeAttackingMob = (INFFGirlsGaiaChargeAttackingMob)(tamed.asMob());
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.allowAllStatesExceptWait();
        this.setInterruptCondition(NFFGirlsAIUtils.predicateFollowingFurtherThan(24d));
    }

    public NFFGirlsGaiaFlyingChargeAttackGoal(INFFTamed tamed) {
        this(tamed, 1.0d);
    }

    public INFFGirlsGaiaChargeAttackingMob getChargeAttacker() {
        return chargeAttackingMob;
    }

    public double getMaxStartingDistance() {
        return maxStartingDistance;
    }

    public NFFGirlsGaiaFlyingChargeAttackGoal setMaxStartingDistance(double maxStartingDistance) {
        this.maxStartingDistance = maxStartingDistance;
        return this;
    }

    public double getMinStartingDistance() {
        return minStartingDistance;
    }

    public NFFGirlsGaiaFlyingChargeAttackGoal setMinStartingDistance(double minStartingDistance) {
        this.minStartingDistance = minStartingDistance;
        return this;
    }

    public RangedRandomDouble getWithdrawalDistanceRange() {
        return withdrawalDistanceRange;
    }

    public NFFGirlsGaiaFlyingChargeAttackGoal setWithdrawalDistanceRange(double min, double max) {
        this.withdrawalDistanceRange = RangedRandomDouble.uniform(min, max);
        return this;
    }

    public int getAttackInterval() {
        return attackInterval;
    }

    public NFFGirlsGaiaFlyingChargeAttackGoal setAttackInterval(int attackInterval) {
        this.attackInterval = attackInterval;
        return this;
    }

    public double getStoppingDistance() {
        return stoppingDistance;
    }

    public NFFGirlsGaiaFlyingChargeAttackGoal setStoppingDistance(double stoppingDistance) {
        this.stoppingDistance = stoppingDistance;
        return this;
    }

    @Override
    public boolean checkCanUse() {
        if (this.mob.asMob().getTarget() != null && this.mob.asMob().getRandom().nextInt(reducedTickDelay(7)) == 0) {
            double distSqr = this.mob.asMob().distanceToSqr(this.mob.asMob().getTarget());
            return distSqr <= this.maxStartingDistance * this.maxStartingDistance
                && distSqr >= this.minStartingDistance * this.minStartingDistance;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkCanContinueToUse() {
        return this.mob.asMob().getTarget() != null
            && this.mob.asMob().getTarget().isAlive()
            && this.mob.asMob().distanceToSqr(this.mob.asMob().getTarget()) <= this.stoppingDistance * this.stoppingDistance;
    }

    @Override
    public void onStart() {
        LivingEntity livingentity = this.mob.asMob().getTarget();
        if (livingentity != null) {
            Vec3 vec3 = livingentity.getEyePosition();
            this.mob.asMob().getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, speedModifier);
        }
        this.chargeAttackingMob.setIsCharging(true);
        this.getChargeAttacker().playChargeAttackSound();
    }

    @Override
    public void onStop() {
        this.chargeAttackingMob.setIsCharging(false);
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void onTick() {
        LivingEntity target = this.mob.asMob().getTarget();
        if (target == null) return;
        // When approaching the target
        if (this.getChargeAttacker().isCharging()) {
            // When reached the target, deal damage and add a random withdrawing movement
            if (NFUMathStatics.getBoxSurfaceDistSqr(this.mob.asMob().getBoundingBox(), target.getBoundingBox()) < 0.04d) {
                this.mob.asMob().doHurtTarget(target);
                this.getChargeAttacker().setIsCharging(false);
                // Add a random movement to withdraw from the attacking position, preparing the next attack
                double yaw = NFUMathStatics.rndRangedDouble(-Math.PI, Math.PI);
                double pitch = NFUMathStatics.rndRangedDouble(Math.PI / 6d, Math.PI / 4d);
                Vec3 movement = new Vec3(Math.cos(yaw), 0, Math.sin(yaw))
                    .scale(Math.cos(pitch))
                    .add(0d, Math.sin(pitch), 0d)
                    .scale(withdrawalDistanceRange.get());
                Vec3 wantedPos = this.mob.asMob().position().add(movement);
                this.mob.asMob().getMoveControl().setWantedPosition(wantedPos.x, wantedPos.y, wantedPos.z, speedModifier);
                this.attackTimer = this.attackInterval;
            }
            // Otherwise, redirect the target pos
            else {
                Vec3 vec3 = target.getBoundingBox().getCenter();
                this.mob.asMob().getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, speedModifier);
            }
        }
        // Otherwise, wait for a cool-down and restart attacking
        else {
            if (this.attackTimer <= 0) {
                startChargeAttack();
            } else {
                --this.attackTimer;
            }
        }
    }

    protected void startChargeAttack() {
        LivingEntity livingentity = this.mob.asMob().getTarget();
        if (livingentity != null) {
            Vec3 vec3 = livingentity.getEyePosition();
            this.mob.asMob().getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, speedModifier);
        }
        this.getChargeAttacker().setIsCharging(true);
        this.getChargeAttacker().playChargeAttackSound();
    }


}
