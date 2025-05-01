package net.sodiumzh.nff.girls.gaia.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaItems;
import net.sodiumzh.nfu.item.NFUItem;

public class EvilGrindstoneItem extends NFUItem {

    public EvilGrindstoneItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return NFFGirlsGaiaItems.EVIL_GRINDSTONE.get().getDefaultInstance();
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}
