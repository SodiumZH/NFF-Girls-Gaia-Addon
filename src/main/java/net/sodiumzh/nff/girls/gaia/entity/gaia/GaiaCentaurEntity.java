package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.Centaur;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.sodiumzh.nautils.entity.MobApplicableItemTable;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import org.jetbrains.annotations.Nullable;

public class GaiaCentaurEntity extends Centaur implements INFFGirlsTamed {

    public GaiaCentaurEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public NFFTamedMobInventory createAdditionalInventory() {
        return null;
    }

    @Nullable
    @Override
    public NFFTamedInventoryMenu makeMenu(int i, Inventory inventory, Container container) {
        return null;
    }

    @Override
    public MobApplicableItemTable getHealingItems() {
        return null;
    }
}
