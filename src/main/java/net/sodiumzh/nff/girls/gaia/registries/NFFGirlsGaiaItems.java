package net.sodiumzh.nff.girls.gaia.registries;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nautils.compat.ModDependencyFallbackItem;
import net.sodiumzh.nautils.statics.NaUtilsCompatStatics;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.item.EvilGrindstoneItem;
import net.sodiumzh.nff.girls.gaia.item.NFFGirlsGaiaCitadelBookItem;
import net.sodiumzh.nff.girls.registry.NFFGirlsTabs;

import java.util.HashSet;
import java.util.Optional;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsGaiaItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NFFGirlsGaia.MOD_ID);
    public static final HashSet<RegistryObject<? extends Item>> NO_TAB = new HashSet<>();

    public static RegistryObject<Item> registerModDependent(String path, String dependingModId, Supplier<? extends Item> itemSupplier) {
        var item = NaUtilsCompatStatics.registerModDependent(ITEMS, path, dependingModId, itemSupplier);
        return item.map(registryObject -> (RegistryObject<Item>) (registryObject))
            .orElseGet(() -> ITEMS.register(path, () -> new ModDependencyFallbackItem(dependingModId, new Item.Properties())));
    }

    protected static <T extends Item> RegistryObject<T> register(String name, Supplier<T> itemSupplier)
    {
        return ITEMS.register(name, itemSupplier);
    }

    protected static <T extends Item> RegistryObject<T> registerNoTab(String name, Supplier<T> itemSupplier)
    {
        RegistryObject<T> res = ITEMS.register(name, itemSupplier);
        NO_TAB.add(res);
        return res;
    }

    public static final RegistryObject<EvilGrindstoneItem> EVIL_GRINDSTONE = register("evil_grindstone",
        () -> new EvilGrindstoneItem(new Item.Properties().stacksTo(1)).descTranslatable("desc.nffgirlsgaia.evil_grindstone").cast());

    public static final RegistryObject<Item> MOB_DICT = registerModDependent("mob_dictionary", "citadel",
        () -> new NFFGirlsGaiaCitadelBookItem(new Item.Properties()));


    @SubscribeEvent
    public static void putTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab().equals(NFFGirlsTabs.TAB.get()))
            for (var item: ITEMS.getEntries()) {
                if (!NO_TAB.contains(item)) event.accept(item);
            }
    }

}
