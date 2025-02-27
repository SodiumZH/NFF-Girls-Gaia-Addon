package net.sodiumzh.nff.girls.gaia.subsystems.baubles;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.gaia.GaiaDryadEntity;
import net.sodiumzh.nff.girls.subsystem.baublesystem.NFFGirlsBaubleRegistrations;
import net.sodiumzh.nff.services.subsystems.baublesystem.RegisterBaubleEquippableMobsEvent;

@EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaBaubles
{
	@SubscribeEvent
	public static void baubleEquippableRegistration(RegisterBaubleEquippableMobsEvent event)
	{
		NFFGirlsBaubleRegistrations.registerWithContinuousSlotSequence(event, GaiaDryadEntity.class, 0, 3);
	}
}
