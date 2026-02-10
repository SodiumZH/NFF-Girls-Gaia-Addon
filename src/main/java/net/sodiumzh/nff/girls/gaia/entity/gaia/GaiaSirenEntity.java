package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Siren;
import gaia.entity.goal.MobAttackGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.gaia.entity.IHasRareVariant;
import net.sodiumzh.nff.girls.inventory.NFFGirlsSkeletonInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithEquipment;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GaiaSirenEntity extends Siren implements INFFGirlsTamed, IBlocksGaiaDynamicGoals, IHasRareVariant {

    private static final EntityDataAccessor<Integer> RARE_VARIANT =
        SynchedEntityData.defineId(GaiaSirenEntity.class, EntityDataSerializers.INT);

    public GaiaSirenEntity(EntityType<? extends GaiaSirenEntity> entityType, Level level) {
        super(entityType, level);
        this.entityData.define(RARE_VARIANT, -1);
    }

    @Override
    public List<Class<? extends Goal>> getGoalsToRemove() {
        return List.of(RangedBowAttackGoal.class, MobAttackGoal.class);
    }

    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return new NFFTamedMobInventoryWithEquipment(9, this);
    }

    @Override
    public NFFTamedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
        return new NFFGirlsSkeletonInventoryMenu(containerId, playerInventory, container, this);
    }

    private static final IHasRareVariant.RareVariant VARIANT_LADINA =
        new IHasRareVariant.RareVariant("ladina", 0.02d, "entity.nffgirlsgaia.variant.ladina");
    private static final List<IHasRareVariant.RareVariant> RARE_VARIANTS = List.of(VARIANT_LADINA);

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
