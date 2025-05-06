package net.sodiumzh.nff.girls.gaia.registry;

import net.sodiumzh.nff.girls.entity.tamingprocesses.hmag.HmagAlrauneTamingProcess;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.entity.tamingprocess.GaiaAnimalTamingProcess;
import net.sodiumzh.nff.girls.registry.NFFGirlsTamingItems;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nff.services.registry.NFFRegistries;
import net.sodiumzh.nfu.registry.NFURegistry;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsGaiaTamingProcesses {

    public static final NFURegistryEntryCollection<NFFTamingProcess> TAMING_PROCESSES =
            NFURegistryEntryCollection.create(NFFRegistries.TAMING_PROCESSES, NFFGirlsGaia.MOD_ID);

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_DRYAD = TAMING_PROCESSES.register(
            "gaia_dryad",  () -> new HmagAlrauneTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.PLANT_B));

    public static final NFURegistry.Accessor<NFFTamingProcess> GAIA_ANIMAL_A = TAMING_PROCESSES.register(
        "gaia_animal_a",  () -> new GaiaAnimalTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.ANIMAL_A));


}
