package net.sodiumzh.nff.girls.gaia.entity.gaia;

import gaia.entity.SludgeGirl;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;
import net.sodiumzh.nff.services.inventory.NFFTamedMobInventory;
import net.sodiumzh.nfu.exception.UnimplementedException;
import org.jetbrains.annotations.Nullable;

public class GaiaSludgeGirlEntity extends SludgeGirl implements INFFGirlsTamed {

    public GaiaSludgeGirlEntity(EntityType<? extends GaiaSludgeGirlEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @Nullable NFFTamedMobInventory createAdditionalInventory() {
        throw new UnimplementedException();
    }

    @Override
    public @Nullable NFFTamedInventoryMenu makeMenu(int i, Inventory inventory, Container container) {
        return null;
    }
}
