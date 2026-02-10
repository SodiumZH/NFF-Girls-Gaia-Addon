package net.sodiumzh.nff.girls.gaia.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.WebBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaEffects;
import net.sodiumzh.nfu.mixin.event.entity.EntityStuckInBlockEvent;

public class CobwebAffinityEffect extends MobEffect {

    public CobwebAffinityEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventListeners {

        @SubscribeEvent
        public static void onStuckInBlock(EntityStuckInBlockEvent event) {
            if (event.getEntity() instanceof LivingEntity l
                && l.hasEffect(NFFGirlsGaiaEffects.COBWEB_AFFINITY.get())
                && event.getBlockState().getBlock() instanceof WebBlock)
            {
                event.setCanceled(true);
            }
        }


    }
}
