package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.effect.AntPheromoneEffect;
import net.sodiumzh.nfu.math.LinearColor;

public class NFFGirlsGaiaEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(
        ForgeRegistries.MOB_EFFECTS, NFFGirlsGaia.MOD_ID);

    public static final RegistryObject<MobEffect> ANT_PHEROMONE = EFFECTS.register("ant_pheromone", () ->
        new AntPheromoneEffect(MobEffectCategory.BENEFICIAL, LinearColor.fromRGB(129, 128, 73).toCode()));
}
