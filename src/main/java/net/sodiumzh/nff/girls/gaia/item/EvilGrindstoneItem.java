package net.sodiumzh.nff.girls.gaia.item;

import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.gaia.entity.IHasRareVariant;
import net.sodiumzh.nfu.item.NFUItem;
import net.sodiumzh.nff.girls.gaia.registry.*;

public class EvilGrindstoneItem extends NFUItem {

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

    @Override
    public InteractionResult interactLivingEntity(Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof IHasRareVariant hrv
            && INFFGirlsTamed.get(target).filter(t -> player.equals(t.getOwnerInDimension())).isPresent()
            && hrv.isRareVariant())
        {
            hrv.setRareVariantID(-1);
            return InteractionResult.sidedSuccess(player.getLevel().isClientSide());
        }
        return InteractionResult.PASS;
    }
}
