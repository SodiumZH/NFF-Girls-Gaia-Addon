package net.sodiumzh.nff.girls.gaia.client;

import gaia.client.renderer.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.registries.NFFGirlsGaiaEntityTypes;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaClientSetupEventHandlers
{
	
	@SubscribeEvent
	public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_DRYAD.get(), DryadRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_SPRIGGAN.get(), SprigganRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_DULLAHAN.get(), DullahanRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_HARPY.get(), HarpyRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_BANSHEE.get(), BansheeRenderer::new);
		event.registerEntityRenderer(NFFGirlsGaiaEntityTypes.GAIA_SUCCUBUS.get(), SuccubusRenderer::new);
	}
}