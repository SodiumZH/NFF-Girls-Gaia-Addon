package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Arachne;
import gaia.entity.goal.MobAttackGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.entity.IHasRareVariant;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaEffects;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsTwoBaublesInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class GaiaArachneEntity extends Arachne implements INFFGirlsTamed, IBlocksGaiaDynamicGoals, IHasRareVariant {

    private static final EntityDataAccessor<Integer> RARE_VARIANT =
        SynchedEntityData.defineId(GaiaArachneEntity.class, EntityDataSerializers.INT);

    public GaiaArachneEntity(EntityType<? extends GaiaArachneEntity> entityType, Level level) {
        super(entityType, level);
        this.entityData.define(RARE_VARIANT, -1);
    }

    @Override
    public List<Class<? extends Goal>> getGoalsToRemove() {
        return List.of(RangedAttackGoal.class, MobAttackGoal.class);
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
    public void aiStep() {
        super.aiStep();
        Player owner = this.getOwnerInDimension();
        if (owner != null && this.hasLineOfSight(owner) && owner.distanceToSqr(this) <= 256d) {
            owner.addEffect(new MobEffectInstance(NFFGirlsGaiaEffects.COBWEB_AFFINITY.get(), 10), this);
        }
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
    }

    private static final IHasRareVariant.RareVariant VARIANT_CALAMITY =
        new IHasRareVariant.RareVariant("calamity", 0.02d, "entity.nffgirlsgaia.variant.calamity");
    private static final List<IHasRareVariant.RareVariant> RARE_VARIANTS = List.of(VARIANT_CALAMITY);

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

    // Block enemy features

    @Override
    protected void beaconMonster(int range, Consumer<LivingEntity> action) {
        super.beaconMonster(range, living -> {
            if (this.isAllyTo(living))
                action.accept(living);
        });
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {
    }

}
