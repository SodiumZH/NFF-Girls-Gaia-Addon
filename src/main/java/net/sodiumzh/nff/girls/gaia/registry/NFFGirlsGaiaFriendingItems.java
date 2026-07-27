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
        NFURegistryEntryCollection.create(NFFGirlsFriendingItems.REGISTRY, NFFGirlsGaia.MOD_ID);

    public static NFURegistry.Accessor<MobApplicableItemTable> AQUATIC_A =
        COLLECTION.register("aquatic_a", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirlsGaia.MOD_ID, "friending/aquatic_a.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());

    public static NFURegistry.Accessor<MobApplicableItemTable> WITCH =
        COLLECTION.register("witch", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirlsGaia.MOD_ID, "friending/witch.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());

    public static NFURegistry.Accessor<MobApplicableItemTable> ENDERMAN_B =
        COLLECTION.register("enderman_b", () -> MobApplicableItemTable.builder()
            .readData(new ResourceLocation(NFFGirlsGaia.MOD_ID, "friending/enderman_b.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
            .build());

    public static NFURegistry.Accessor<MobApplicableItemTable> ARTHROPOD =
            COLLECTION.register("arthropod", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirlsGaia.MOD_ID, "friending/arthropod.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());

    public static NFURegistry.Accessor<MobApplicableItemTable> HUMANOID_A =
            COLLECTION.register("humanoid_a", () -> MobApplicableItemTable.builder()
                    .readData(new ResourceLocation(NFFGirlsGaia.MOD_ID, "friending/humanoid_a.json"), NFFGirlsDataReaders::readMobApplicableItemTable)
                    .build());
}
