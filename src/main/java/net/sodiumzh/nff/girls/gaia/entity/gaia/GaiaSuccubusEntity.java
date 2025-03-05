package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Succubus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.sodiumzh.nautils.entity.MobApplicableItemTable;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;
import net.sodiumzh.nautils.statics.NaUtilsTagStatics;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsLocateBlockGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.hmag.HmagImpEntity;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsTwoBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagImpInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.goal.preset.*;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;

import java.util.Arrays;
import java.util.Collection;

public class GaiaSuccubusEntity extends Succubus implements INFFGirlsTamed {

    public GaiaSuccubusEntity(EntityType<? extends GaiaSuccubusEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 0;
        Arrays.fill(this.armorDropChances, 0.0F);
        Arrays.fill(this.handDropChances, 0.0F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new NFFMeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(4, new NFFGirlsFollowOwnerGoal(this, 1.0, 5.0F, 2.0F, false));
        this.goalSelector.addGoal(5, new NFFWaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NFFOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NFFOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
        this.targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
    }

    public MobApplicableItemTable getHealingItems() {
        return  NFFGirlsHealingItems.GENERAL_HUMANOID_0.get();
    }

    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventoryWithHandItems(4, this);
    }

    public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
        return new NFFGirlsHandItemsTwoBaublesInventoryMenu(containerId, playerInventory, container, this);
    }

    protected SoundEvent getAmbientSound() {
        return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
    }
}
