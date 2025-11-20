package net.sodiumzh.nff.girls.gaia.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class VexLikeMoveControl extends MoveControl {

    public double horizontalDeceleration = 0.5d;
    public double verticalDeceleration = 0.9d;
    public double speedScale = 1.0d;

    public VexLikeMoveControl(Mob mob) {
        super(mob);
    }

    public void tick() {
        Vec3 movementVec = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
        boolean arrived = movementVec.length() < this.mob.getBoundingBox().getSize();
        if (arrived) {
            Vec3 oldVel = this.mob.getDeltaMovement();
            this.mob.setDeltaMovement(oldVel.multiply(1d - horizontalDeceleration, 1d - verticalDeceleration, 1d - horizontalDeceleration));
        }
        if (this.operation == MoveControl.Operation.MOVE_TO) {
            if (arrived) {
                this.operation = MoveControl.Operation.WAIT;
            } else {
                double speedAttr = Optional.ofNullable(this.mob.getAttribute(Attributes.FLYING_SPEED))
                    .map(AttributeInstance::getValue).orElse(1.0d);
                this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(movementVec.scale(this.speedModifier * this.speedScale * speedAttr * 0.05 / movementVec.length())));
                if (this.mob.getTarget() == null) {
                    Vec3 vec31 = this.mob.getDeltaMovement();
                    this.mob.setYRot(-((float) Mth.atan2(vec31.x, vec31.z)) * 57.295776F);
                    this.mob.yBodyRot = this.mob.getYRot();
                } else {
                    double d2 = this.mob.getTarget().getX() - this.mob.getX();
                    double d1 = this.mob.getTarget().getZ() - this.mob.getZ();
                    this.mob.setYRot(-((float)Mth.atan2(d2, d1)) * 57.295776F);
                    this.mob.yBodyRot = this.mob.getYRot();
                }
            }
        }
    }

    /**
     * When reached the wanted position, the horizontal velocity will be reduced by this proportion every tick.
     * Default 0.5.
     */
    public double getHorizontalDeceleration() {
        return horizontalDeceleration;
    }

    /**
     * When reached the wanted position, the horizontal velocity will be reduced by this proportion every tick.
     * Default 0.5.
     */
    public VexLikeMoveControl setHorizontalDeceleration(double deceleration) {
        this.horizontalDeceleration = deceleration;
        return this;
    }

    /**
     * When reached the wanted position, the vertical velocity will be reduced by this proportion every tick.
     * Default 0.5.
     */
    public double getVerticalDeceleration() {
        return verticalDeceleration;
    }

    /**
     * When reached the wanted position, the vertical velocity will be reduced by this proportion every tick.
     * Default 0.5.
     */
    public VexLikeMoveControl setVerticalDeceleration(double deceleration) {
        this.verticalDeceleration = deceleration;
        return this;
    }

    /**
     * Delta speed will be multiplied by this value.
     */
    public double getSpeedScale() {
        return speedScale;
    }

    /**
     * Delta speed will be multiplied by this value.
     */
    public VexLikeMoveControl setSpeedScale(double speedScale) {
        this.speedScale = speedScale;
        return this;
    }
}
