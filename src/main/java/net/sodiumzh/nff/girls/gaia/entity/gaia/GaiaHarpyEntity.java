package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Harpy;
import gaia.entity.goal.MobAttackGoal;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.inventory.NFFGirlsFourBaublesInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFLeapAtOwnerGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFLeapAtTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class GaiaHarpyEntity extends Harpy implements INFFGirlsTamed, IBlocksGaiaDynamicGoals {

    public GaiaHarpyEntity(EntityType<? extends GaiaHarpyEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 0;
        Arrays.fill(this.armorDropChances, 0);
        Arrays.fill(this.handDropChances, 0);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new NFFLeapAtTargetGoal(this, 0.39F, 0.45F, 32.0F, 24));
        goalSelector.addGoal(3, new NFFMeleeAttackGoal(this, 1.0d, true));
        goalSelector.addGoal(4, new NFFLeapAtOwnerGoal(this, 0.39F, 0.45F, 32.0F, 24));
        goalSelector.addGoal(5, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
        goalSelector.addGoal(6, new NFFWaterAvoidingRandomStrollGoal(this, 1.0d));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
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
    public MobApplicableItemTable getHealingItems()
    {
        return NFFGirlsHealingItems.ANIMAL.get();
    }

    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventory(4, this);
    }

    @Override
    public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
        return new NFFGirlsFourBaublesInventoryMenu(containerId, playerInventory, container, this);
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
    }

    @Override
    @Nonnull
    public Component getTypeName() {
        EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
        return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
    }

    @Override
    public List<Class<? extends Goal>> getGoalsToRemove() {
        return List.of(LeapAtTargetGoal.class, MobAttackGoal.class, AvoidEntityGoal.class);
    }
}
