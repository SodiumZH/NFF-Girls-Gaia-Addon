package net.sodiumzh.nff.girls.gaia.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nfu.item.NFUItem;

public class EvilGrindstoneItem extends NFUItem {

    public EvilGrindstoneItem(Item.Properties pProperties) {
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
