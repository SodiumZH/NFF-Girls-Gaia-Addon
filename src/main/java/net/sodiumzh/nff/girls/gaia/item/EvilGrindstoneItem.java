package net.sodiumzh.nff.girls.gaia.item;

import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.item.NaUtilsItem;
import net.sodiumzh.nff.girls.gaia.registries.NFFGirlsGaiaItems;

public class EvilGrindstoneItem extends NaUtilsItem {

    public EvilGrindstoneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return this.getDefaultInstance();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }
}
