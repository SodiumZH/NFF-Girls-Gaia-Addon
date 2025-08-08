package net.sodiumzh.nff.girls.gaia.registry;

import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.sodiumzh.nfu.util.NFUTagStatics;

public class NFFGirlsGaiaTags {

    public static final TagKey<Item> GAIA_LOOT_TROPHIES_NORMAL = NFUTagStatics.createItemTag(
        NFFGirlsGaia.MOD_ID, "gaia_loot_trophies_normal");
    public static final TagKey<Item> GAIA_LOOT_TROPHIES_RARE = NFUTagStatics.createItemTag(
        NFFGirlsGaia.MOD_ID, "gaia_loot_trophies_rare");
    public static final TagKey<Item> GAIA_LOOT_WEAPON_BOOKS = NFUTagStatics.createItemTag(
        NFFGirlsGaia.MOD_ID, "gaia_loot_weapon_books");
    public static final TagKey<Item> GAIA_LOOT_MOB_WEAPONS_NORMAL = NFUTagStatics.createItemTag(
        NFFGirlsGaia.MOD_ID, "gaia_loot_mob_weapons_normal");
    public static final TagKey<Item> GAIA_LOOT_MOB_WEAPONS_RARE = NFUTagStatics.createItemTag(
        NFFGirlsGaia.MOD_ID, "gaia_loot_mob_weapons_rare");
    public static final TagKey<Item> GAIA_LOOT_BOXES_TIER_0 = NFUTagStatics.createItemTag(
        NFFGirlsGaia.MOD_ID, "gaia_loot_boxes_tier_0");
    public static final TagKey<Item> GAIA_LOOT_BOXES_TIER_1 = NFUTagStatics.createItemTag(
        NFFGirlsGaia.MOD_ID, "gaia_loot_boxes_tier_1");
    public static final TagKey<Item> GAIA_LOOT_BOXES_TIER_2 = NFUTagStatics.createItemTag(
        NFFGirlsGaia.MOD_ID, "gaia_loot_boxes_tier_2");

    public static final TagKey<Item> WEAPON_FANS = NFUTagStatics.createItemTag(
        NFFGirlsGaia.MOD_ID, "weapon_fans");
    public static final TagKey<Item> WEAPON_STAFFS = NFUTagStatics.createItemTag(
        NFFGirlsGaia.MOD_ID, "weapon_staffs");

    public static final TagKey<EntityType<?>> CAN_DISABLE_DAY_SPAWN = NFUTagStatics.createEntityTypeTag(
        NFFGirlsGaia.MOD_ID, "can_disable_day_spawn");
}
