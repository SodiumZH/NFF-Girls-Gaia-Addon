package net.sodiumzh.nff.girls.gaia.item;

import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.item.NaUtilsItem;
import net.sodiumzh.nff.girls.gaia.registries.NFFGirlsGaiaItems;

public class EvilGrindstoneItem extends NaUtilsItem {

    public EvilGrindstoneItem(Properties pProperties) {
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
