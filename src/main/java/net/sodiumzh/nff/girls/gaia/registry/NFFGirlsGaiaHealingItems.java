package net.sodiumzh.nff.girls.gaia.registry;

import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nfu.entity.MobApplicableItemTable;
import net.sodiumzh.nfu.registry.NFURegistries;
import net.sodiumzh.nfu.registry.NFURegistryEntryCollection;

public class NFFGirlsGaiaHealingItems
{
	private static final NFURegistryEntryCollection<MobApplicableItemTable> HEALING_ITEMS =
			NFURegistryEntryCollection.create(NFURegistries.MOB_APPLICABLE_ITEM_TABLES, NFFGirlsGaia.MOD_ID);

}
