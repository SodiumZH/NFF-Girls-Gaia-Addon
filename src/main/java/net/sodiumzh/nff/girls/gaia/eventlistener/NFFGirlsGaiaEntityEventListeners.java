package net.sodiumzh.nff.girls.gaia.eventlistener;

import gaia.capability.CapabilityHandler;
import gaia.entity.AbstractGaiaEntity;
import gaia.registry.GaiaRegistry;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.gaia.GaiaMummyEntity;
import net.sodiumzh.nff.girls.gaia.event.GaiaMobFinalizeSpawnEvent;
import net.sodiumzh.nff.services.event.entity.NFFMobTamedEvent;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NFFGirlsGaiaEntityEventListeners
{
	// BM events
	@SubscribeEvent
	public static void onTamed(NFFMobTamedEvent event)
	{
		if (event.mobBefore instanceof AbstractGaiaEntity before 
				&& INFFGirlsTamed.isBM(event.mobBefriended)
				&& event.mobBefriended instanceof AbstractGaiaEntity after)
		{
			after.setBaby(before.isBaby());
			after.setVariant(before.getVariant());
			INFFGirlsTamed.ifBM(after, tamed -> {
				after.getCapability(CapabilityHandler.CAPABILITY_FRIENDED).ifPresent((cap) -> {
					cap.setFriendly(true);
					cap.setFriendedBy(tamed.getOwnerUUID());
					after.setPersistenceRequired();
				});
			});
		}
	}

	// TODO this event listener is merged from HmagBansheeTamingProcess as the listener in that class only handle HMaG banshee. Merge this to NFFGirls and remove in the next version.
	/*@Deprecated
	@SubscribeEvent
	public static void preventWitherInProcess(MobEffectEvent.Applicable event) {
		if (event.getEffectInstance().getEffect().equals(MobEffects.WITHER)) {
			LivingEntity var2 = event.getEntity();
			if (var2 instanceof Mob e) {
				if (CNFFTamable.getOptional(e).map((tamable) -> {
					return tamable.getTamingProcess() instanceof HmagBansheeTamingProcess;
				}).orElse(false) && CNFFTamable.get(e).getTamingProcess().isInAnyProcess(e)) {
					event.setResult(Event.Result.DENY);
				}
			}
		}
	}*/

	@SubscribeEvent
	public static void onJoinLevel(EntityJoinWorldEvent event) {
		// Prevent Gravemite spawn from friended Mummy
		if (event.getEntity().getType().equals(GaiaRegistry.GRAVEMITE.getEntityType())) {
			// Search by stacktrace
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
			for (StackTraceElement e: stacktrace) {
				if (e.getClassName().equals(GaiaMummyEntity.class.getName()))
					event.setCanceled(true);
			}
		}
	}

	// GAIA Mixin events
	@SubscribeEvent
	public static void onGaiaFinalizeSpawn(GaiaMobFinalizeSpawnEvent event)
	{
		if (INFFGirlsTamed.isBM(event.getEntity()))
			event.setCanceled(true);
	}
}
