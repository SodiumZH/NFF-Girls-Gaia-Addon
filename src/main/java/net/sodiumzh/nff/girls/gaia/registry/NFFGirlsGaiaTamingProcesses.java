package net.sodiumzh.nff.girls.gaia.registry;

import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.tamingprocess.GaiaAnimalTamingProcess;
import net.sodiumzh.nff.girls.gaia.entity.tamingprocess.GaiaAquaticTamingProcess;
import net.sodiumzh.nff.girls.gaia.entity.tamingprocess.GaiaValkyrieTamingProcess;
import net.sodiumzh.nff.girls.gaia.entity.tamingprocess.GaiaYukiOnnaTamingProcess;
import net.sodiumzh.nff.girls.registry.NFFGirlsTamingItems;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nff.services.registry.NFFRegistries;

public class NFFGirlsGaiaTamingProcesses {

    public static final NFURegistryEntryCollection<NFFTamingProcess> TAMING_PROCESSES =
            NFURegistryEntryCollection.create(NFFRegistries.TAMING_PROCESSES, NFFGirlsGaia.MOD_ID);

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_DRYAD = TAMING_PROCESSES.register(
            "gaia_dryad",  () -> new HmagAlrauneTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.PLANT_B));

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_ANIMAL_A = TAMING_PROCESSES.register(
        "gaia_animal_a",  () -> new GaiaAnimalTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.ANIMAL_A));

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_VALKYRIE = TAMING_PROCESSES.register(
        "gaia_valkyrie", GaiaValkyrieTamingProcess::new);

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_YUKI_ONNA = TAMING_PROCESSES.register(
        "gaia_yuki_onna", () -> new GaiaYukiOnnaTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.SNOWMAN));

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_AQUATIC_A = TAMING_PROCESSES.register(
        "gaia_aquatic_a", () -> new GaiaAquaticTamingProcess().setItemGivingTableOverride(NFFGirlsGaiaTamingItems.AQUATIC_A));

}
