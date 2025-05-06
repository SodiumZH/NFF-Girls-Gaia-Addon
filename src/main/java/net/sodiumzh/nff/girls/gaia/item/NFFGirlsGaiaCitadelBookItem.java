package net.sodiumzh.nff.girls.gaia.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumzh.nff.girls.gaia.client.gui.NFFGirlsGaiaCitadelMobDictScreen;
import net.sodiumzh.nfu.item.NFUItem;

// TODO modify nffgirls book and use that instead
@Deprecated
public class NFFGirlsGaiaCitadelBookItem extends NFUItem {

    public NFFGirlsGaiaCitadelBookItem(Item.Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemStackIn = playerIn.getItemInHand(handIn);
        if (worldIn.isClientSide) {
            NFFGirlsGaiaCitadelMobDictScreen.openGUI(itemStackIn);
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, itemStackIn);
    }

}
