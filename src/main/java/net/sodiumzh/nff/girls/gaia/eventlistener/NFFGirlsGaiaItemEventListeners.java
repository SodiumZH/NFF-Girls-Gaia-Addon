package net.sodiumzh.nff.girls.gaia.eventlistener;

import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import gaia.item.edible.MonsterFeedItem;
import gaia.registry.GaiaRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaItems;
import net.sodiumzh.nfu.util.NFUInfoStatics;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsGaiaItemEventListeners {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().is(GaiaRegistry.MONSTER_FEED.get())) {
            Component name = event.getToolTip().get(0);
            event.getToolTip().clear();
            event.getToolTip().add(name);
            event.getToolTip().add(NFUInfoStatics.createTranslatable("desc.nffgirlsgaia.monster_feed", 10).withStyle(ChatFormatting.WHITE));
        }
        else if (event.getItemStack().is(GaiaRegistry.PREMIUM_MONSTER_FEED.get())) {
            Component name = event.getToolTip().get(0);
            event.getToolTip().clear();
            event.getToolTip().add(name);
            event.getToolTip().add(NFUInfoStatics.createTranslatable("desc.nffgirlsgaia.monster_feed", 100).withStyle(ChatFormatting.WHITE));
        }
    }

}
