package net.sodiumzh.nff.girls.gaia.registry;

import net.minecraft.resources.ResourceLocation;
import net.sodiumzh.nff.girls.data.NFFGirlsDataReaders;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.registry.NFFGirlsFriendingItems;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsGaiaFriendingItems {

    public static NFURegistryEntryCollection<MobApplicableItemTable> COLLECTION =
        NFURegistryEntryCollection.create(NFFGirlsFriendingItems.FRIENDING_ITEMS, NFFGirlsGaia.MOD_ID);

    public static NFURegistry.Accessor<MobApplicableItemTable> AQUATIC_A =
        COLLECTION.register("aquatic_a", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirlsGaia.MOD_ID, "friending/aquatic_a.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());


}
