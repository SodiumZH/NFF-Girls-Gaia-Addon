package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItemMappings;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaHealingItemMappings {

    @SubscribeEvent
    public static void registerHealingItems(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NFFGirlsHealingItemMappings.getTable().put(NFFGirlsGaiaEntityTypes.GAIA_BANSHEE.get(), NFFGirlsHealingItems.UNDEAD);
            NFFGirlsHealingItemMappings.getTable().put(NFFGirlsGaiaEntityTypes.GAIA_MERMAID.get(), NFFGirlsGaiaHealingItems.AQUATIC);
            NFFGirlsHealingItemMappings.getTable().put(NFFGirlsGaiaEntityTypes.GAIA_VALKYRIE.get(), NFFGirlsHealingItems.GENERAL_HUMANOID_0);
            NFFGirlsHealingItemMappings.getTable().put(NFFGirlsGaiaEntityTypes.GAIA_DRYAD.get(), NFFGirlsHealingItems.PLANT);
            NFFGirlsHealingItemMappings.getTable().put(NFFGirlsGaiaEntityTypes.GAIA_CECAELIA.get(), NFFGirlsGaiaHealingItems.AQUATIC);
            NFFGirlsHealingItemMappings.getTable().put(NFFGirlsGaiaEntityTypes.GAIA_DULLAHAN.get(), NFFGirlsHealingItems.UNDEAD);
            NFFGirlsHealingItemMappings.getTable().put(NFFGirlsGaiaEntityTypes.GAIA_HARPY.get(), NFFGirlsHealingItems.ANIMAL);
            NFFGirlsHealingItemMappings.getTable().put(NFFGirlsGaiaEntityTypes.GAIA_MUMMY.get(), NFFGirlsHealingItems.UNDEAD);
            NFFGirlsHealingItemMappings.getTable().put(NFFGirlsGaiaEntityTypes.GAIA_SUCCUBUS.get(), NFFGirlsHealingItems.GENERAL_HUMANOID_0);
            NFFGirlsHealingItemMappings.getTable().put(NFFGirlsGaiaEntityTypes.GAIA_SPRIGGAN.get(), NFFGirlsHealingItems.PLANT);
            NFFGirlsHealingItemMappings.getTable().put(NFFGirlsGaiaEntityTypes.GAIA_YUKI_ONNA.get(), NFFGirlsHealingItems.SNOWMAN);
        });
    }

}
