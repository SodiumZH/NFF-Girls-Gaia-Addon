package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Spriggan;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nautils.entity.MobApplicableItemTable;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtByTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtTargetGoal;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHmagThreeBaublesInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.goal.preset.*;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFOwnerHurtByTargetGoal;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class GaiaSprigganEntity extends Spriggan implements INFFGirlsTamed {

    public GaiaSprigganEntity(EntityType<? extends GaiaSprigganEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 0;
        Arrays.fill(this.armorDropChances, 0);
        Arrays.fill(this.handDropChances, 0);
    }

    /* AI */

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(4, new NFFMeleeAttackGoal(this, 1.0d, true));
        goalSelector.addGoal(5, new NFFGirlsFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
        goalSelector.addGoal(6, new NFFWaterAvoidingRandomStrollGoal(this, 1.0d));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NFFOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
        targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
        targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
    }

	@Override
	public void aiStep()
	{
		super.aiStep();
        if (this.hasEffect(MobEffects.INVISIBILITY) && this.getEffect(MobEffects.INVISIBILITY).getDuration() <= 20)
            this.removeEffect(MobEffects.INVISIBILITY);
	}

    /* Interaction */

    // Map items that can heal the mob and healing values here.
    // Leave it empty if you don't need healing features.
    @Override
    public MobApplicableItemTable getHealingItems()
    {
        return NFFGirlsHealingItems.PLANT.get();
    }

    /* Inventory */

    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventoryWithHandItems(3, this);
    }

    @Override
    public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
        return new NFFGirlsHmagThreeBaublesInventoryMenu(containerId, playerInventory, container, this);
    }

    /* Save and Load */

    @Override
    protected SoundEvent getAmbientSound()
    {
        return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
    }

}
