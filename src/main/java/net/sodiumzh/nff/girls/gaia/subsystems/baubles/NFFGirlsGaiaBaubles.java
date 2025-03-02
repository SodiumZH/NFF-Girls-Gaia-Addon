package net.sodiumzh.nff.girls.gaia.subsystems.baubles;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.sodiumzh.nff.girls.entity.hmag.HmagDullahanEntity;
import net.sodiumzh.nff.girls.entity.hmag.HmagHarpyEntity;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.gaia.*;
import net.sodiumzh.nff.girls.subsystem.baublesystem.NFFGirlsBaubleRegistrations;
import net.sodiumzh.nff.services.subsystem.baublesystem.RegisterBaubleEquippableMobsEvent;

@EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaBaubles
{
	@SubscribeEvent
	public static void baubleEquippableRegistration(RegisterBaubleEquippableMobsEvent event)
	{
		NFFGirlsBaubleRegistrations.registerWithContinuousSlotSequence(event, GaiaDryadEntity.class, 0, 3);
		NFFGirlsBaubleRegistrations.registerWithContinuousSlotSequence(event, GaiaSprigganEntity.class, 0, 3);
		NFFGirlsBaubleRegistrations.registerWithContinuousSlotSequence(event, GaiaDullahanEntity.class, 2, 6);
		NFFGirlsBaubleRegistrations.registerWithContinuousSlotSequence(event, GaiaHarpyEntity.class, 0, 4);
		NFFGirlsBaubleRegistrations.registerWithContinuousSlotSequence(event, GaiaBansheeEntity.class, 0, 3);
		NFFGirlsBaubleRegistrations.registerWithContinuousSlotSequence(event, GaiaSuccubusEntity.class, 2, 4);
	}
}
