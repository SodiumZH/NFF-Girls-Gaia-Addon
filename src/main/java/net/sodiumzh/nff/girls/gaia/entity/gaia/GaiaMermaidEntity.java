package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Cecaelia;
import gaia.entity.Mermaid;
import gaia.entity.goal.MobAttackGoal;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsRangedAttackGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtByTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtTargetGoal;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.entity.ai.NFFGirlsGaiaAmphibiousMoveControl;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaHealingItems;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsFourBaublesDefaultInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsTwoBaublesInventoryMenu;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFAmphibiousGoals;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRandomSwimGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.entity.taming.INFFTamedAmphibious;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.util.NFULevelStatics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GaiaMermaidEntity extends Mermaid implements INFFGirlsTamed, INFFTamedAmphibious {
    public GaiaMermaidEntity(EntityType<? extends GaiaMermaidEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new NFFGirlsGaiaAmphibiousMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(3, new NFFMeleeAttackGoal(this, 1.0D, false));
        goalSelector.addGoal(4, new NFFAmphibiousGoals.FollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false, 2).amphibious());
        goalSelector.addGoal(5, new NFFAmphibiousGoals.GoToBeachGoal(this, 1.0D));
        goalSelector.addGoal(6, new NFFAmphibiousGoals.SwimUpGoal(this, 1.0D, this.level().getSeaLevel()));
        goalSelector.addGoal(7, new NFFRandomStrollGoal(this, 1.0d));
        goalSelector.addGoal(7, new NFFRandomSwimGoal(this, 1.0d, 120));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
        targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
        targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
        targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
    }

    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventoryWithHandItems(4);
    }

    @Nullable
    @Override
    public NFFTamedInventoryMenu makeMenu(int i, Inventory inventory, Container container) {
        return new NFFGirlsHandItemsTwoBaublesInventoryMenu(i, inventory, container, this);
    }

    @Override
    public MobApplicableItemTable getHealingItems() {
        return NFFGirlsGaiaHealingItems.AQUATIC.get();
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
        if (!this.level().isClientSide) {
            if (this.isEffectiveAi() && this.isInWater() && NFULevelStatics.getWaterDepth(this) > 2) {
                this.switchNav(true);
                this.setSwimming(true);
            } else {
                this.switchNav(false);
                this.setSwimming(false);
            }
        }
    }

    @Override
    public boolean shouldSitOnWaiting() {
        return false;
    }

}
