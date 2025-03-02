package net.sodiumzh.nff.girls.gaia.registries;

import gaia.GrimoireOfGaia;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.registry.NFFGirlsTamingProcesses;
import net.sodiumzh.nff.services.event.setup.NFFTamingMappingRegisterEvent;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaTamingMappings
{
	@SubscribeEvent
	public static void registerTamingMapping(NFFTamingMappingRegisterEvent event)
	{
		event.register(
				new ResourceLocation(GrimoireOfGaia.MOD_ID, "dryad"),
				new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_dryad"),
				NFFGirlsGaiaTamingProcesses.GAIA_DRYAD::get);
		event.register(
				new ResourceLocation(GrimoireOfGaia.MOD_ID, "spriggan"),
				new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_spriggan"),
				NFFGirlsTamingProcesses.HMAG_ALRAUNE::get);
		event.register(
				new ResourceLocation(GrimoireOfGaia.MOD_ID, "dullahan"),
				new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_dullahan"),
				NFFGirlsTamingProcesses.VANILLA_UNDEAD_B::get);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "harpy"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_harpy"),
			NFFGirlsGaiaTamingProcesses.GAIA_ANIMAL_A::get);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "banshee"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_banshee"),
			NFFGirlsTamingProcesses.HMAG_BANSHEE::get);
		event.register(
			new ResourceLocation(GrimoireOfGaia.MOD_ID, "succubus"),
			new ResourceLocation(NFFGirlsGaia.MOD_ID, "gaia_succubus"),
			NFFGirlsTamingProcesses.HMAG_IMP::get);
	}
}
