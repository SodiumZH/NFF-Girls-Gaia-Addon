package net.sodiumzh.nff.girls.gaia.registries;

import java.util.function.Function;

import gaia.entity.Dullahan;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.gaia.*;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaEntityTypes extends NFFGirlsEntityTypes
{
	
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NFFGirlsGaia.MOD_ID);

	public static final RegistryObject<EntityType<GaiaDryadEntity>> GAIA_DRYAD = 
			registerBM("gaia_dryad", GaiaDryadEntity::new, builder -> builder
			.sized(0.6F, 1.99F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false));
	
	public static final RegistryObject<EntityType<GaiaSprigganEntity>> GAIA_SPRIGGAN =
			registerBM("gaia_spriggan", GaiaSprigganEntity::new, builder -> builder
			.sized(0.6F, 1.6F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false));

	public static final RegistryObject<EntityType<GaiaDullahanEntity>> GAIA_DULLAHAN =
			registerBM("gaia_dullahan", GaiaDullahanEntity::new, builder -> builder
			.sized(0.6F, 1.6F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false));

	public static final RegistryObject<EntityType<GaiaHarpyEntity>> GAIA_HARPY =
		registerBM("gaia_harpy", GaiaHarpyEntity::new, builder -> builder
			.sized(0.6F, 1.99F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false));

	public static final RegistryObject<EntityType<GaiaBansheeEntity>> GAIA_BANSHEE =
		registerBM("gaia_banshee", GaiaBansheeEntity::new, builder -> builder
			.sized(0.6F, 1.99F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false));

	public static final RegistryObject<EntityType<GaiaSuccubusEntity>> GAIA_SUCCUBUS =
		registerBM("gaia_succubus", GaiaSuccubusEntity::new, builder -> builder
			.sized(0.6F, 1.99F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false));



	@SubscribeEvent
	public static void onAttributeCreate(EntityAttributeCreationEvent event) {	
		
		NFFGirlsGaiaEntityAttributes.REGISTRY.forEach((type, supplier) ->
		{
			event.put(type, supplier.get().build());
		});
	
	}
	
	private static <T extends LivingEntity> RegistryObject<EntityType<T>> registerBM(String regName, 
			EntityType.EntityFactory<T> creator, Function<EntityType.Builder<T>, EntityType.Builder<T>> builderModifier)
	{
		return NFFGirlsEntityTypes.registerBM(ENTITY_TYPES, NFFGirlsGaia.MOD_ID, regName, creator, MobCategory.CREATURE, builderModifier);
	}
}
