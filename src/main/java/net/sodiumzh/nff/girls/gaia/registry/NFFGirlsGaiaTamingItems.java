package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.data.NFFGirlsDataReaders;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsGaiaTamingItems {

    public static NFURegistryEntryCollection<MobApplicableItemTable> COLLECTION =
        NFURegistryEntryCollection.create(NFURegistries.MOB_APPLICABLE_ITEM_TABLES, NFFGirlsGaia.MOD_ID);

    public static NFURegistry.Accessor<MobApplicableItemTable> AQUATIC_A =
        COLLECTION.register("taming_aquatic_a", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirlsGaia.MOD_ID, "taming_items/aquatic_a.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());


}
