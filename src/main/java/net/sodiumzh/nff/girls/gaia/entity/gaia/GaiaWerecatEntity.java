package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Werecat;
import gaia.entity.goal.MobAttackGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.ai.goal.NFFGirlsFollowOwnerGoal;
import net.sodiumzh.nff.girls.entity.ai.goal.target.*;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.entity.IHasRareVariant;
import net.sodiumzh.nff.girls.inventory.NFFGirlsFourBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsTwoBaublesInventoryMenu;
import net.sodiumzh.nff.girls.sound.NFFGirlsSoundPresets;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFLeapAtOwnerGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFLeapAtTargetGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFMeleeAttackGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.NFFWaterAvoidingRandomStrollGoal;
import net.sodiumzh.nff.services.entity.ai.goal.preset.target.NFFHurtByTargetGoal;
import net.sodiumzh.nff.services.entity.taming.NFFTamingMapping;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class GaiaWerecatEntity extends Werecat implements INFFGirlsTamed, IBlocksGaiaDynamicGoals, IHasRareVariant {

    private static final EntityDataAccessor<Integer> RARE_VARIANT =
        SynchedEntityData.defineId(GaiaWerecatEntity.class, EntityDataSerializers.INT);

    public GaiaWerecatEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 0;
        Arrays.fill(this.armorDropChances, 0);
        Arrays.fill(this.handDropChances, 0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RARE_VARIANT, -1);
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
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventoryWithHandItems(4, this);
    }

    @Nullable
    @Override
    public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
        return new NFFGirlsHandItemsTwoBaublesInventoryMenu(containerId, playerInventory, container, this);
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

    private static final RareVariant VARIANT_RANA =
        new RareVariant("rana", 0.05d, "entity.nffgirlsgaia.mygo.rana");
    private static final List<RareVariant> RARE_VARIANTS = List.of(VARIANT_RANA);

    @Override
    public int getRareVariantID() {
        return this.entityData.get(RARE_VARIANT);
    }

    @Override
    public void setRareVariantID(int id) {
        this.entityData.set(RARE_VARIANT, id);
    }

    @Override
    public @Nullable RareVariant rareVariantByID(int id) {
        return id >= 0 && id < RARE_VARIANTS.size() ? RARE_VARIANTS.get(id) : null;
    }

}
