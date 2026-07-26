package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.services.entity.taming.NFFTamedTypeRegistry;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaTamedTypes {

    @SubscribeEvent
    public static void registerTamed(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> ForgeRegistries.ENTITY_TYPES.getKeys().stream()
            .filter(key -> key.getNamespace().equals(NFFGirlsGaia.MOD_ID) && key.getPath().startsWith("gaia_"))
            .forEach(key -> NFFTamedTypeRegistry.add(key, NFFTamedTypeRegistry.SELF)));
    }


}
