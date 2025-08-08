package net.sodiumzh.nff.girls.gaia;

import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaEntityTypes;
import net.sodiumzh.nff.girls.gaia.registry.NFFGirlsGaiaItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sodiumzh.nff.girls.gaia.registry.*;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;

@Mod(NFFGirlsGaia.MOD_ID)
public class NFFGirlsGaia
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "nffgirlsgaia";

    public NFFGirlsGaia()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NFFGirlsGaiaConfigs.CONFIG);
        modEventBus.addListener(NFFGirlsGaiaConfigs::loadConfig);

        NFFGirlsGaiaEntityTypes.ENTITY_TYPES.register(modEventBus);
        NFFGirlsGaiaItems.ITEMS.register(modEventBus);
        NFFGirlsGaiaEntityAttributes.ENTITY_ATTRIBUTE_PROVIDERS.merge();
        NFFGirlsGaiaGeometries.FIELD_PATTERNS.merge();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }


}
