package net.sodiumzh.nff.girls.gaia.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.sodiumzh.nfu.util.NFULevelStatics;

public class NFFGirlsGaiaAmphibiousMoveControl extends MoveControl{

    public NFFGirlsGaiaAmphibiousMoveControl(Mob pMob) {
        super(pMob);
    }

    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (this.mob.isInWater() && NFULevelStatics.getWaterDepth(this.mob) > 2) {
            if (livingentity != null && livingentity.getY() > this.mob.getY()) {
                this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, 0.002, 0.0));
            }

            if (this.operation != MoveControl.Operation.MOVE_TO || this.mob.getNavigation().isDone()) {
                this.mob.setSpeed(0.0F);
                return;
            }

            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedY - this.mob.getY();
            double d2 = this.wantedZ - this.mob.getZ();
            double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
            d1 /= d3;
            float f = (float)(Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 90.0F));
            this.mob.yBodyRot = this.mob.getYRot();
            float f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            float f2 = Mth.lerp(0.125F, this.mob.getSpeed(), f1);
            this.mob.setSpeed(f2);
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add((double)f2 * d0 * 0.005, (double)f2 * d1 * 0.1, (double)f2 * d2 * 0.005));
        } else {
            if (!this.mob.isOnGround()) {
                this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, -0.008, 0.0));
            }

            super.tick();
        }

    }
}
