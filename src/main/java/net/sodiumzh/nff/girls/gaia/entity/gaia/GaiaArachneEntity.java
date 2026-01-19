package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Arachne;
import gaia.entity.goal.MobAttackGoal;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.gaia.entity.IBlocksGaiaDynamicGoals;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsTwoBaublesInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventoryWithHandItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GaiaArachneEntity extends Arachne implements INFFGirlsTamed, IBlocksGaiaDynamicGoals {

    public GaiaArachneEntity(EntityType<? extends GaiaArachneEntity> entityType, Level level) {
        super(entityType, level);
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
}
