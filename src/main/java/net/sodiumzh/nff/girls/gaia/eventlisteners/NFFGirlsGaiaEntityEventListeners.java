package net.sodiumzh.nff.girls.gaia.eventlisteners;

import gaia.capability.CapabilityHandler;
import gaia.entity.AbstractGaiaEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.entity.tamingprocesses.hmag.HmagBansheeTamingProcess;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.events.GaiaMobFinalizeSpawnEvent;
import net.sodiumzh.nff.services.entity.capability.CNFFTamable;
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
	@Deprecated
	@SubscribeEvent
	public static void preventWitherInProcess(MobEffectEvent.Applicable event) {
		if (event.getEffectInstance().getEffect().equals(MobEffects.WITHER)) {
			LivingEntity var2 = event.getEntity();
			if (var2 instanceof Mob e) {
				if ((Boolean) CNFFTamable.getOptional(e).map((tamable) -> {
					return tamable.getTamingProcess() instanceof HmagBansheeTamingProcess;
				}).orElse(false) && CNFFTamable.get(e).getTamingProcess().isInAnyProcess(e)) {
					event.setResult(Event.Result.DENY);
				}
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
