package net.sodiumzh.nff.girls.gaia.registries;

import java.util.HashMap;
import java.util.function.Supplier;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityAttributes;

public class NFFGirlsGaiaEntityAttributes extends NFFGirlsEntityAttributes
{
	
	public static final HashMap<EntityType<? extends LivingEntity>, Supplier<AttributeSupplier.Builder>> REGISTRY = new HashMap<>();
	
	public static Supplier<AttributeSupplier.Builder> register(Supplier<AttributeSupplier.Builder> builderSupplier, EntityType<? extends LivingEntity> type)
	{
		REGISTRY.put(type, builderSupplier);
		return builderSupplier;
	}
	
	public static final Supplier<AttributeSupplier.Builder> HMAG_ZOMBIE_GIRL_ATTRIBUTES = register(() ->
		NFFGirlsEntityAttributes.VANILLA_MONSTER_COMMON_ATTRIBUTES.get()
		.add(Attributes.MAX_HEALTH, 40.0)
		.add(Attributes.FOLLOW_RANGE, 40.0)
		.add(Attributes.MOVEMENT_SPEED, 0.25)
		.add(Attributes.ATTACK_DAMAGE, 4.0)
		.add(Attributes.ARMOR, 4.0)
		.add(Attributes.ATTACK_KNOCKBACK, 0.3)
		.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.0),
		NFFGirlsGaiaEntityTypes.GAIA_DRYAD.get());

	
}
