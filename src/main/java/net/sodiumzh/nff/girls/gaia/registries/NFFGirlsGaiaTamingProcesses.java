package net.sodiumzh.nff.girls.gaia.registries;

import net.sodiumzh.nautils.registries.NaUtilsRegistry;
import net.sodiumzh.nautils.registries.RegistryEntryCollection;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.tamingprocesses.hmag.HmagAlrauneTamingProcess;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.registry.NFFGirlsTamingItems;
import net.sodiumzh.nff.services.entity.taming.NFFTamingProcess;
import net.sodiumzh.nff.services.registry.NFFRegistries;

public class NFFGirlsGaiaTamingProcesses {

    public static final RegistryEntryCollection<NFFTamingProcess> TAMING_PROCESSES =
            RegistryEntryCollection.create(NFFRegistries.TAMING_PROCESSES, NFFGirlsGaia.MOD_ID);

    public static final NaUtilsRegistry.Accessor<NFFTamingProcess> GAIA_DRYAD = TAMING_PROCESSES.register(
            "gaia_dryad",  () -> new HmagAlrauneTamingProcess().setItemGivingTableOverride(NFFGirlsTamingItems.PLANT_A::get));


}
