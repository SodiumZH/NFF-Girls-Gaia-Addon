package net.sodiumzh.nff.girls.gaia.registries;

import gaia.GrimoireOfGaia;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.services.event.setup.NFFTamingMappingRegisterEvent;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaTamingMappings
{
	@SubscribeEvent
	public static void registerBefriendingType(NFFTamingMappingRegisterEvent event)
	{
		event.register(
				new ResourceLocation(GrimoireOfGaia.MOD_ID, "dryad"),
				new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_dryad"),
				NFFGirlsGaiaTamingProcesses.GAIA_DRYAD::get);
	}
}
