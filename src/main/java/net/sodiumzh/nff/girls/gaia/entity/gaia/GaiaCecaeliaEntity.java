package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Cecaelia;
import gaia.entity.goal.MobAttackGoal;
import gaia.registry.GaiaSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsRangedAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtByTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsTridentAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.entity.ai.NFFGirlsGaiaAmphibiousMoveControl;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaHealingItems;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaProjectileProviders;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaTags;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsFourBaublesDefaultInventoryMenu;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.preset.*;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.INFFTamedAmphibious;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.entity.NFUEffectZoneEntity;
import net.sodiumzh.nfu.util.NFULevelStatics;
import net.sodiumzh.nfu.util.NFUReflectionStatics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GaiaCecaeliaEntity extends Cecaelia implements INFFGirlsTamed, IBlocksGaiaDynamicGoals, INFFTamedAmphibious {
    public GaiaCecaeliaEntity(EntityType<? extends GaiaCecaeliaEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new NFFGirlsGaiaAmphibiousMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(2, new GaiaCecaeliaEntity.CastGoal(this, 1.0D, 3 * 20, 15.0F));
        goalSelector.addGoal(3, new NFFMeleeAttackGoal(this, 1.0D, false));
        goalSelector.addGoal(4, new NFFAmphibiousGoals.FollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false, 2).amphibious());
        goalSelector.addGoal(5, new NFFAmphibiousGoals.GoToBeachGoal(this, 1.0D));
        goalSelector.addGoal(6, new NFFAmphibiousGoals.SwimUpGoal(this, 1.0D, this.level.getSeaLevel()));
        goalSelector.addGoal(7, new NFFRandomStrollGoal(this, 1.0d));
        goalSelector.addGoal(7, new NFFRandomSwimGoal(this, 1.0d, 120));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
        targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
        targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
        targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(7, new NFFGirlsNearestPotentiallyHostileToSelfTargetGoal(this));
        targetSelector.addGoal(8, new NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(9, new NFFGirlsAttackingStrategyTargetGoal(this));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (this.getRandom().nextDouble() < 0.2d) {
            var p = NFFGirlsGaiaProjectileProviders.BUBBLE_SPHERE.apply(this);
            p.alignCenterTo(this.getEyePosition());
            this.level.addFreshEntity(p);
            p.shootTo(target.getBoundingBox().getCenter(), 0.4f, 1f);
        } else {
            var p = NFFGirlsGaiaProjectileProviders.BUBBLE_BOMB_PROJECTILE.apply(this);
            p.setPos(this.getEyePosition());
            this.level.addFreshEntity(p);
            p.shootTo(target.getBoundingBox().getCenter(), 0.6f, 1f);
        }
        this.setThrowing(true);
        this.swing(InteractionHand.MAIN_HAND);
        this.playSound(GaiaSounds.GAIA_SHOOT.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventoryWithHandItems(6);
    }

    @Nullable
    @Override
    public NFFTamedInventoryMenu makeMenu(int i, Inventory inventory, Container container) {
        return new NFFGirlsHandItemsFourBaublesDefaultInventoryMenu(i, inventory, container, this);
    }

    @Override
    public List<Class<? extends Goal>> getGoalsToRemove() {
        return List.of(RangedAttackGoal.class, MobAttackGoal.class);
    }

    @Override
    public WaterBoundPathNavigation getWaterNav() {
        return this.waterNavigation;
    }

    @Override
    public GroundPathNavigation getGroundNav() {
        return this.groundNavigation;
    }

    @Override
    public PathNavigation getAppliedNav() {
        return this.navigation;
    }

    @Override
    public void switchNav(boolean b) {
        this.navigation = b ? this.waterNavigation :  this.groundNavigation;
    }

    @Override
    public void updateSwimming() {
        if (!this.level.isClientSide) {
            if (this.isEffectiveAi() && this.isInWater() && NFULevelStatics.getWaterDepth(this) > 2) {
                this.switchNav(true);
                this.setSwimming(true);
            } else {
                this.switchNav(false);
                this.setSwimming(false);
            }
        }
    }

    /**
     * Disable original weapon switching mechanism which leads to item loss.
     * See {@link Cecaelia#aiStep()}
     */
    @Override
    protected boolean playerDetection(int range, TargetingConditions conditions) {
        return false;
    }

    @Override
    public boolean shouldSitOnWaiting() {
        return false;
    }

    public static class CastGoal extends NFFGirlsRangedAttackGoal {
        private GaiaCecaeliaEntity e;

        public CastGoal(INFFTamed mob, double pSpeedModifier, int pAttackInterval, float pAttackRadius) {
            super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
            this.e = (GaiaCecaeliaEntity) mob.asMob();
        }

        private boolean canCast() {
            return e.isInWater() && e.getTarget() != null && e.getTarget().isInWater() && e.getTarget().distanceToSqr(e) >= 16d;
        }
        @Override
        public boolean checkCanUse() {
            return super.checkCanUse() && canCast();
        }
        @Override
        public boolean checkCanContinueToUse() {
            return super.checkCanContinueToUse() && canCast();
        }
        @Override
        public void onStart() {
            super.onStart();
            e.setThrowing(true);
        }
        @Override
        public void onStop() {
            super.onStop();
            e.setThrowing(false);
        }
    }

}
