package net.sodiumzh.nff.girls.gaia.item;

import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.item.NaUtilsItem;

public class EvilGrindstoneItem extends NaUtilsItem {

    public EvilGrindstoneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.copy();
    }

}
