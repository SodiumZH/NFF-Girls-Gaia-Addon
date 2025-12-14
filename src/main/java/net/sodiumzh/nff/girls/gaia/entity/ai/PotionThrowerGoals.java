package net.sodiumzh.nff.girls.gaia.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.sodiumzh.nff.girls.gaia.entity.IPotionThrower;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRangedAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFShootProjectileGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class PotionThrowerGoals {

    public static class PotionAttackGoal extends NFFRangedAttackGoal {

        protected IPotionThrower potionThrower;

        public PotionAttackGoal(INFFTamed mob, double pSpeedModifier, int pAttackInterval, float pAttackRadius) {
            super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
            this.potionThrower = (IPotionThrower) mob;
        }

        protected IPotionThrower getPotionThrower() {
            return (IPotionThrower) this.getMob();
        }

        public boolean checkCanUse() {
            return super.checkCanUse() && this.potionThrower.isInCombat();
        }
    }

    public static class PotionSupportGoal extends NFFShootProjectileGoal {

        protected IPotionThrower potionThrower;

        public PotionSupportGoal(INFFTamed mob, double pSpeedModifier, int pAttackInterval, float pAttackRadius) {
            super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
            this.potionThrower = (IPotionThrower) mob;
            this.allowAllStatesExceptWait();
        }

        @Override
        protected void performShooting(LivingEntity livingEntity, float v) {
            if (this.target == null) return;
            potionThrower.throwPotion(this.target, v, false);
        }

        @Override
        protected LivingEntity updateTarget() {
            return potionThrower.getTargetingAlly().orElse(null);
        }

        public boolean checkCanUse() {
            return super.checkCanUse() && this.potionThrower.isInCombat();
        }
    }

    public static class PotionEmergencySupportGoal extends NFFShootProjectileGoal {

        protected IPotionThrower potionThrower;

        public PotionEmergencySupportGoal(INFFTamed mob, double pSpeedModifier, int pAttackInterval, float pAttackRadius) {
            super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
            this.potionThrower = (IPotionThrower) mob;
            this.allowAllStates();
        }

        public boolean checkCanUse() {
            return super.checkCanUse() && this.potionThrower.isInCombat();
        }

        @Override
        protected void performShooting(LivingEntity livingEntity, float v) {
            if (this.target == null) return;
            potionThrower.throwPotion(this.target, v, false);
        }

        @Override
        protected LivingEntity updateTarget() {
            return potionThrower.getAllyInEmergency().orElse(null);
        }
    }

    public static class PotionIdleSupportGoal extends NFFShootProjectileGoal {

        protected IPotionThrower potionThrower;

        public PotionIdleSupportGoal(INFFTamed mob, double pSpeedModifier, int pAttackInterval, float pAttackRadius) {
            super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
            this.potionThrower = (IPotionThrower) mob;
            this.allowAllStates();
        }

        public boolean checkCanUse() {
            return super.checkCanUse() && !this.potionThrower.isInCombat();
        }

        @Override
        protected void performShooting(LivingEntity livingEntity, float v) {
            if (this.target == null) return;
            potionThrower.throwPotion(this.target, v, false);
        }

        @Override
        protected LivingEntity updateTarget() {
            return potionThrower.getIdleHealingAlly().orElse(null);
        }
    }

}
