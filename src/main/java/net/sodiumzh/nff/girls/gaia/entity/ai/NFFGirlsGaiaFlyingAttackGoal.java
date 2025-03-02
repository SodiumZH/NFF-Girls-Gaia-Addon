package net.sodiumzh.nff.girls.gaia.entity.ai;

import gaia.entity.Banshee;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.sodiumzh.nff.services.entity.ai.NFFTamedMobAIState;
import net.sodiumzh.nff.services.entity.ai.goal.NFFGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

import java.util.EnumSet;

public class NFFGirlsGaiaFlyingAttackGoal extends NFFGoal {

    protected double minStartAttackDistance = 2.0d;

    public NFFGirlsGaiaFlyingAttackGoal(INFFTamed mob) {
        super(mob);
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.allowAllStatesExceptWait();
    }

    public boolean checkCanUse() {
        if (this.getMob().asMob().getTarget() != null
            && !this.getMob().asMob().getMoveControl().hasWanted()
            && this.getMob().asMob().getRandom().nextInt(reducedTickDelay(7)) == 0)
        {
            return this.getMob().asMob().distanceToSqr(this.getMob().asMob().getTarget()) > minStartAttackDistance * minStartAttackDistance;
        }
        else {
            return false;
        }
    }

    public boolean checkCanContinueToUse() {
        return this.getMob().asMob().getMoveControl().hasWanted()/* && this.banshee.isCharging()*/
            && this.getMob().asMob().getTarget() != null
            && this.getMob().asMob().getTarget().isAlive();
    }

    public void onStart() {
        LivingEntity livingentity = this.getMob().asMob().getTarget();
        if (livingentity != null) {
            Vec3 vec3 = livingentity.getEyePosition();
            this.getMob().asMob().getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0);
        }
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void onTick() {
        LivingEntity livingentity = this.getMob().asMob().getTarget();
        if (livingentity != null) {
            if (this.getMob().asMob().getBoundingBox().intersects(livingentity.getBoundingBox())) {
                this.getMob().asMob().doHurtTarget(livingentity);
                this.onHurtTarget();
            } else {
                double d0 = this.getMob().asMob().distanceToSqr(livingentity);
                if (d0 < 9.0) {
                    Vec3 vec3 = livingentity.getEyePosition();
                    this.getMob().asMob().getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0);
                }
            }
        }

    }

    protected void onHurtTarget() {}

}
