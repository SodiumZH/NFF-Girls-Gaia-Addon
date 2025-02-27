package net.sodiumzh.nff.girls.gaia.events;

import gaia.entity.AbstractGaiaEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.events.hooks.GaiaMobFinalizeSpawnEvent;
import net.sodiumzh.nff.services.event.entity.NFFMobTamedEvent;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsGaiaEntityEvents
{
	// BM events
	@SubscribeEvent
	public static void onBefriended(NFFMobTamedEvent event)
	{
		if (event.mobBefore instanceof AbstractGaiaEntity before 
				&& INFFGirlsTamed.isBM(event.mobBefriended)
				&& event.mobBefriended.asMob() instanceof AbstractGaiaEntity after)
		{
			after.setBaby(before.isBaby());
			after.setVariant(before.getVariant());
		}
	}
	
	// DG Mixin events
	@SubscribeEvent
	public static void onGaiaFinalizeSpawn(GaiaMobFinalizeSpawnEvent event)
	{
		if (INFFGirlsTamed.isBM(event.getEntity()))
			event.setCanceled(true);
	}
}
