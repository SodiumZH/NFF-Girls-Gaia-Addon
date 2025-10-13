package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Dullahan;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsFourBaublesDefaultInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFFleeSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFRestrictSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class GaiaDullahanEntity extends Dullahan implements INFFGirlsTamed {

    public GaiaDullahanEntity(EntityType<? extends GaiaDullahanEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 0;
        Arrays.fill(this.armorDropChances, 0.0F);
        Arrays.fill(this.handDropChances, 0.0F);
    }


    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new NFFRestrictSunGoal(this));
        this.goalSelector.addGoal(3, new NFFFleeSunGoal(this, 1.0));
        this.goalSelector.addGoal(4, new NFFMeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(5, (new NFFGirlsFollowOwnerGoal(this, 1.0, 5.0F, 2.0F, false)).avoidSunCondition(NFFGirlsEntityStatics::isSunSensitive));
        this.goalSelector.addGoal(6, new NFFWaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NFFHurtByTargetGoal(this));
        targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
        targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
        targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(7, new NFFGirlsNearestPotentiallyHostileToSelfTargetGoal(this));
        targetSelector.addGoal(8, new NFFGirlsNearestPotentiallyHostileToOwnerTargetGoal(this));
        targetSelector.addGoal(9, new NFFGirlsAttackingStrategyTargetGoal(this));
    }

    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventoryWithHandItems(6, this);
    }

    public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
        return new NFFGirlsHandItemsFourBaublesDefaultInventoryMenu(containerId, playerInventory, container, this);
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        NFFTamedStatics.readBefriendedCommonSaveData(this, nbt);
        this.setInit();
    }

    protected SoundEvent getAmbientSound() {
        return NFFGirlsSoundPresets.generalAmbient(super.getAmbientSound());
    }

    @Override
    @Nonnull
    public Component getTypeName() {
        EntityType<?> typeBefore = NFFTamingMapping.getTypeBefore(this);
        return typeBefore != null ? typeBefore.getDescription() : super.getTypeName();
    }
}
