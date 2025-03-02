package net.sodiumzh.nff.girls.gaia.entity.gaia;

import com.github.mechalopa.hmag.world.entity.DullahanEntity;
import gaia.entity.Dullahan;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
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
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToOwnerTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsNearestHostileToSelfTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtByTargetGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.NFFGirlsOwnerHurtTargetGoal;
import net.sodiumzh.nff.girls.entity.hmag.HmagDullahanEntity;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsFourBaublesDefaultInventoryMenu;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.girls.util.NFFGirlsEntityStatics;
import net.sodiumzh.nff.services.entity.ai.goal.presets.NFFFleeSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.presets.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.presets.NFFRestrictSunGoal;
import net.sodiumzh.nff.services.entity.ai.goal.presets.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.presets.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamedStatics;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;

import java.util.Arrays;
import java.util.UUID;

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
        this.targetSelector.addGoal(1, new NFFGirlsOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NFFHurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(3, new NFFGirlsOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(5, new NFFGirlsNearestHostileToSelfTargetGoal(this));
        this.targetSelector.addGoal(6, new NFFGirlsNearestHostileToOwnerTargetGoal(this));
    }

    public MobApplicableItemTable getHealingItems() {
        return NFFGirlsHealingItems.UNDEAD.get();
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

}
