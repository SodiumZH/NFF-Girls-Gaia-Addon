package net.sodiumzh.nff.girls.gaia;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sodiumzh.nff.girls.gaia.registries.NFFGirlsGaiaEntityTypes;

@Mod(NFFGirlsGaia.MOD_ID)
public class NFFGirlsGaia
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "nffgirlsgaia";

    public NFFGirlsGaia()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        NFFGirlsGaiaEntityTypes.ENTITY_TYPES.register(modEventBus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }


}
