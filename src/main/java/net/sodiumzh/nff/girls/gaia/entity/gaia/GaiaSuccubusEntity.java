package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Succubus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.entity.IHasRareVariant;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsTwoBaublesInventoryMenu;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import net.sodiumzh.nfu.util.NFUInfoStatics;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class GaiaSuccubusEntity extends Succubus implements INFFGirlsTamed, IHasRareVariant {

    private static final EntityDataAccessor<Integer> RARE_VARIANT =
        SynchedEntityData.defineId(GaiaSuccubusEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RARE_VARIANT, -1);
    }

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
        return new NFFTamedMobInventoryWithHandItems(4, this);
    }

    public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
        return new NFFGirlsHandItemsTwoBaublesInventoryMenu(containerId, playerInventory, container, this);
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


    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("rareVariant", this.getRareVariantID());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("rareVariant", Tag.TAG_ANY_NUMERIC))
            this.setRareVariantID(nbt.getInt("rareVariant"));
            // Port legacy MyGO data
        else if (nbt.contains("myGO", Tag.TAG_ANY_NUMERIC) && nbt.getBoolean("myGO"))
            this.setRareVariantID(0);
    }

    private static final IHasRareVariant.RareVariant VARIANT_TOMORI =
        new IHasRareVariant.RareVariant("tomori", 0.05d, "entity.nffgirlsgaia.mygo.tomori");
    private static final List<IHasRareVariant.RareVariant> RARE_VARIANTS = List.of(VARIANT_TOMORI);

    @Override
    public int getRareVariantID() {
        return this.entityData.get(RARE_VARIANT);
    }

    @Override
    public void setRareVariantID(int id) {
        this.entityData.set(RARE_VARIANT, id);
    }

    @Override
    public @Nullable IHasRareVariant.RareVariant rareVariantByID(int id) {
        return id >= 0 && id < RARE_VARIANTS.size() ? RARE_VARIANTS.get(id) : null;
    }

}
