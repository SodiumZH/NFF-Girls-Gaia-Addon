package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaConfigs {

    protected static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec CONFIG;



    // Tweaks
    public static ForgeConfigSpec.BooleanValue EXPLOSIVE_PROJECTILE_DESTROYS_ITEMS;
    public static ForgeConfigSpec.BooleanValue SPAWNS_MALE_MOBS;
    public static ForgeConfigSpec.DoubleValue CECAELIA_SPAWN_RATE;
    public static ForgeConfigSpec.BooleanValue ALLOWS_DAY_HOSTILE_MOB_SPAWN_ON_GROUND;

    static {
        BUILDER.push("tweak");
        EXPLOSIVE_PROJECTILE_DESTROYS_ITEMS = BUILDER.comment("If true, Gaia's explosive projectile (not including those from " +
            "friended ones) will destroy dropped items.")
            .define("explosiveProjectileDestroysItems", true);
        SPAWNS_MALE_MOBS = BUILDER.comment("Whether to spawn male variants.")
            .define("spawnsMaleMobs", true);
        CECAELIA_SPAWN_RATE = BUILDER.comment("Modify this to reduce the spawn of Cecaelia. Its spawn rate (0-1) is multiplied by this value.")
                .defineInRange("cecaeliaSpawnRate", 1d, 0d, 1d);
        ALLOWS_DAY_HOSTILE_MOB_SPAWN_ON_GROUND = BUILDER.comment("If false, Gaia hostile mobs will not spawn on ground in the daytime. The mob list can be configured by entity type tag \"can_config_no_day_spawn\"")
                .define("allowsDayHostileMobSpawnOnGround", true);
        BUILDER.pop();
        CONFIG = BUILDER.build();
    }

    public static class ValueCache {

        public static class Tweak {
            public static boolean EXPLOSIVE_PROJECTILE_DESTROYS_ITEMS;
            public static boolean SPAWNS_MALE_MOBS;
            public static double CECAELIA_SPAWN_RATE;
            public static boolean ALLOWS_DAY_HOSTILE_MOB_SPAWN_ON_GROUND;
        }

        public static void refreshCommon() {
            Tweak.EXPLOSIVE_PROJECTILE_DESTROYS_ITEMS = EXPLOSIVE_PROJECTILE_DESTROYS_ITEMS.get();
            Tweak.SPAWNS_MALE_MOBS = SPAWNS_MALE_MOBS.get();
            Tweak.CECAELIA_SPAWN_RATE = CECAELIA_SPAWN_RATE.get();
            Tweak.ALLOWS_DAY_HOSTILE_MOB_SPAWN_ON_GROUND = ALLOWS_DAY_HOSTILE_MOB_SPAWN_ON_GROUND.get();
        }

    }

    @SubscribeEvent
    public static void loadConfig(final ModConfigEvent event)
    {
        if (event.getConfig().getSpec() == CONFIG)
        {
            ValueCache.refreshCommon();
        }
    }

}
