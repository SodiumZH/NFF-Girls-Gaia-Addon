package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.data.NFFGirlsDataReaders;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.registry.NFFGirlsHealingItems;
import net.sodiumzh.nff.services.registry.NFFRegistries;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsGaiaHealingItems {

    public static NFURegistryEntryCollection<MobApplicableItemTable> COLLECTION =
        NFURegistryEntryCollection.create(NFFGirlsHealingItems.HEALING_ITEMS, NFFGirlsGaia.MOD_ID);

    public static NFURegistry.Accessor<MobApplicableItemTable> AQUATIC =
        COLLECTION.register("aquatic", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirlsGaia.MOD_ID, "healing/aquatic.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());




}
