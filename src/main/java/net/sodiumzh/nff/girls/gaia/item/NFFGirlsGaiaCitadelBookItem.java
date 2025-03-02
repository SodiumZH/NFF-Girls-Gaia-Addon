package net.sodiumzh.nff.girls.gaia.item;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumzh.nautils.item.NaUtilsItem;
import net.sodiumzh.nff.girls.client.gui.screen.CitadelBasedMobDictionaryGUI;
import net.sodiumzh.nff.girls.gaia.client.gui.NFFGirlsGaiaCitadelMobDictScreen;

// TODO modify nffgirls book and use that instead
@Deprecated
public class NFFGirlsGaiaCitadelBookItem extends NaUtilsItem {

    public NFFGirlsGaiaCitadelBookItem(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemStackIn = playerIn.getItemInHand(handIn);
        if (worldIn.isClientSide) {
            Minecraft.getInstance().setScreen(new NFFGirlsGaiaCitadelMobDictScreen(itemStackIn));
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, itemStackIn);
    }

}
