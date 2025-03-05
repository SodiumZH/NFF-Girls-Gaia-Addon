package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Banshee;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nautils.entity.MobApplicableItemTable;
import net.sodiumzh.nautils.statics.NaUtilsLevelStatics;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamedSunSensitiveMob;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFlyingFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.gaia.entity.ai.NFFGirlsGaiaFlyingAttackGoal;
import net.sodiumzh.nff.girls.inventory.NFFGirlsThreeBaublesInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.goal.presets.NFFFlyingLandGoal;
import net.sodiumzh.nff.services.entity.ai.goal.presets.NFFFlyingRandomMoveGoal;
import net.sodiumzh.nff.services.entity.ai.goal.presets.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.presets.target.NFFOwnerHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.presets.target.NFFOwnerHurtTargetGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;

import java.util.Arrays;

public class GaiaBansheeEntity extends Banshee implements INFFGirlsTamedSunSensitiveMob {
    /* Initialization */

    public GaiaBansheeEntity(EntityType<? extends GaiaBansheeEntity> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
        this.xpReward = 0;
        Arrays.fill(this.armorDropChances, 0);
        Arrays.fill(this.handDropChances, 0);
    }

    /* AI */

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new GaiaBansheeEntity.ChargeAttackGoal(this));
        this.goalSelector.addGoal(5, new GaiaBansheeEntity.LandGoal(this));
        this.goalSelector.addGoal(6, new NFFGirlsFlyingFollowOwnerGoal(this));
        this.goalSelector.addGoal(8, new NFFFlyingRandomMoveGoal(this).heightLimit(7));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NFFOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NFFOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
        this.targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
    }

    /* Interaction */

    // Map items that can heal the mob and healing values here.
    // Leave it empty if you don't need healing features.
    @Override
    public MobApplicableItemTable getHealingItems() {
        return NFFGirlsHealingItems.UNDEAD.get();
    }

    /** Inventory **/

    // This enables mob armor and hand items by default.
    // If not needed, use NFFTamedMobInventory class instead.

    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventory(3, this);
    }

    @Override
    public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
        return new NFFGirlsThreeBaublesInventoryMenu(containerId, playerInventory, container, this);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
    }

    protected SoundEvent getChargeAttackSound() {return SoundEvents.VEX_CHARGE;}

    public static class ChargeAttackGoal extends NFFGirlsGaiaFlyingAttackGoal {

        protected final GaiaBansheeEntity mobBanshee;

        public ChargeAttackGoal(GaiaBansheeEntity mob) {
            super(mob);
            this.mobBanshee = mob;
        }

        public boolean checkCanContinueToUse() {
            return super.checkCanContinueToUse() && this.mobBanshee.isCharging();
        }

        public void onStart() {
            super.onStart();
            this.mobBanshee.setIsCharging(true);
            this.mobBanshee.playSound(this.mobBanshee.getChargeAttackSound(), 1.0F, 1.0F);
        }

        protected void onHurtTarget() {
            super.onHurtTarget();
            this.mobBanshee.setIsCharging(false);
        }

        public void onStop() {
            super.onStop();
            this.mobBanshee.setIsCharging(false);
        }
    }

    // TODO Correct NFFFlyingLandGoal and use it instead
    @Deprecated
    public static class LandGoal extends NFFFlyingLandGoal {

        public LandGoal(INFFTamed mob) {
            super(mob);
        }

        public void onTick() {
            if (this.mob.isOwnerPresent()) {
                if (!this.mob.asMob().getMoveControl().hasWanted()) {
                    if (!NaUtilsLevelStatics.isAboveVoid(this.mob.asMob().blockPosition(), this.mob.asMob())) {
                        BlockPos pos;
                        for(pos = this.mob.asMob().blockPosition(); this.mob.asMob().level().getBlockState(pos).isAir() && pos.getY() >= this.mob.asMob().level().getMinBuildHeight(); pos = pos.below()) {
                        }

                        pos = pos.above((int) Math.ceil(this.mob.asMob().getEyeHeight()));
                        this.mob.asMob().getMoveControl().setWantedPosition((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), this.speed);
                    }
                }
            }
        }

    }

}
