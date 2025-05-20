package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityAttributes;
import net.sodiumzh.nfu.entity.EntityAttributeProvider;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

import java.util.HashMap;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaEntityAttributes extends NFFGirlsEntityAttributes
{

	public static final NFURegistryEntryCollection<EntityAttributeProvider> ENTITY_ATTRIBUTE_PROVIDERS = 
		NFURegistryEntryCollection.create(NFURegistries.ENTITY_ATTRIBUTE_PROVIDERS, NFFGirlsGaia.MOD_ID);

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_DRYAD = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_dryad", () -> EntityAttributeProvider.monster()
				.add(Attributes.MAX_HEALTH, 40.0)
				.add(Attributes.FOLLOW_RANGE, 40.0)
				.add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 4.0)
				.add(Attributes.ARMOR, 4.0)
				.add(Attributes.ATTACK_KNOCKBACK, 0.3)
				.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_SPRIGGAN = ENTITY_ATTRIBUTE_PROVIDERS.register(
			"gaia_spriggan", () -> EntityAttributeProvider.monster()
				.add(Attributes.MAX_HEALTH, 80.0)
				.add(Attributes.FOLLOW_RANGE, 40.0)
				.add(Attributes.MOVEMENT_SPEED, 0.275)
				.add(Attributes.ATTACK_DAMAGE, 8.0)
				.add(Attributes.ARMOR, 8.0)
				.add(Attributes.ATTACK_KNOCKBACK, 0.25)
				.add((Attribute)ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_DULLAHAN = ENTITY_ATTRIBUTE_PROVIDERS.register(
			"gaia_dullahan", () -> EntityAttributeProvider.monster()
				.add(Attributes.MAX_HEALTH, 40.0)
				.add(Attributes.FOLLOW_RANGE, 40.0)
				.add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 4.0)
				.add(Attributes.ARMOR, 4.0)
				.add(Attributes.ATTACK_KNOCKBACK, 0.3)
				.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_HARPY = ENTITY_ATTRIBUTE_PROVIDERS.register(
			"gaia_harpy", () -> EntityAttributeProvider.monster()
				.add(Attributes.MAX_HEALTH, 40.0)
				.add(Attributes.FOLLOW_RANGE, 40.0)
				.add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 4.0)
				.add(Attributes.ARMOR, 4.0)
				.add(Attributes.ATTACK_KNOCKBACK, 0.3)
				.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_BANSHEE = ENTITY_ATTRIBUTE_PROVIDERS.register(
			"gaia_banshee", () -> EntityAttributeProvider.monster()
				.add(Attributes.MAX_HEALTH, 80.0)
				.add(Attributes.FOLLOW_RANGE, 40.0)
				.add(Attributes.MOVEMENT_SPEED, 0.275)
				.add(Attributes.ATTACK_DAMAGE, 8.0)
				.add(Attributes.ARMOR, 8.0)
				.add(Attributes.ATTACK_KNOCKBACK, 0.25)
				.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_SUCCUBUS = ENTITY_ATTRIBUTE_PROVIDERS.register(
			"gaia_succubus", () -> EntityAttributeProvider.monster()
				.add(Attributes.MAX_HEALTH, 40.0)
				.add(Attributes.FOLLOW_RANGE, 30.0)
				.add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 4.0)
				.add(Attributes.ARMOR, 4.0)
				.add(Attributes.ATTACK_KNOCKBACK, 0.3)
				.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_MUMMY = ENTITY_ATTRIBUTE_PROVIDERS.register(
			"gaia_mummy", () -> EntityAttributeProvider.monster()
				.add(Attributes.MAX_HEALTH, 40.0)
				.add(Attributes.FOLLOW_RANGE, 20.0)
				.add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 4.0)
				.add(Attributes.ARMOR, 4.0)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
				.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	public static final NFURegistry.Accessor<EntityAttributeProvider> GAIA_BEE = ENTITY_ATTRIBUTE_PROVIDERS.register(
		"gaia_bee", () -> EntityAttributeProvider.monster()
			.add(Attributes.MAX_HEALTH, 40.0)
			.add(Attributes.FOLLOW_RANGE, 20.0)
			.add(Attributes.MOVEMENT_SPEED, 0.25)
			.add(Attributes.FLYING_SPEED, 0.5)
			.add(Attributes.ATTACK_DAMAGE, 4.0)
			.add(Attributes.ARMOR, 4.0)
			.add(Attributes.ATTACK_KNOCKBACK, 0.3)
			.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0));

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(NFFGirlsGaiaEntityTypes.GAIA_DRYAD.get(), NFFGirlsGaiaEntityAttributes.GAIA_DRYAD.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_DULLAHAN.get(), NFFGirlsGaiaEntityAttributes.GAIA_DULLAHAN.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_BANSHEE.get(), NFFGirlsGaiaEntityAttributes.GAIA_BANSHEE.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_HARPY.get(), NFFGirlsGaiaEntityAttributes.GAIA_HARPY.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_SPRIGGAN.get(), NFFGirlsGaiaEntityAttributes.GAIA_SPRIGGAN.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_MUMMY.get(), NFFGirlsGaiaEntityAttributes.GAIA_MUMMY.get().get().build());
		event.put(NFFGirlsGaiaEntityTypes.GAIA_SUCCUBUS.get(), NFFGirlsGaiaEntityAttributes.GAIA_SUCCUBUS.get().get().build());
	}

}
