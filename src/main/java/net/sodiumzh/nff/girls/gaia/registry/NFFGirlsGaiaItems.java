package net.sodiumzh.nff.girls.gaia.registry;

import gaia.item.weapon.FanItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nff.girls.gaia.NFFGirlsGaia;
import net.sodiumzh.nff.girls.gaia.item.EvilGrindstoneItem;
import net.sodiumzh.nff.girls.registry.NFFGirlsTabs;
import net.sodiumzh.nfu.compat.ModDependencyFallbackItem;
import net.sodiumzh.nfu.util.NFUCompatStatics;
import net.sodiumzh.nff.girls.item.CitadelBasedMobDictionaryItem;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = NFFGirlsGaia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class  NFFGirlsGaiaItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NFFGirlsGaia.MOD_ID);
    public static final HashSet<RegistryObject<? extends Item>> NO_TAB = new HashSet<>();

    public static RegistryObject<Item> registerModDependent(String path, String dependingModId, Supplier<? extends Item> itemSupplier, @Nullable CreativeModeTab tab) {
        Item.Properties fallbackProperties = tab != null ? new Item.Properties().tab(tab) : new Item.Properties();
        var item = NFUCompatStatics.registerModDependent(ITEMS, path, dependingModId, itemSupplier);
        return item.map(registryObject -> (RegistryObject<Item>) (registryObject))
            .orElseGet(() -> ITEMS.register(path, () -> new ModDependencyFallbackItem(dependingModId, fallbackProperties)));
    }

    protected static <T extends Item> RegistryObject<T> register(String name, Supplier<T> itemSupplier)
    {
        return ITEMS.register(name, itemSupplier);
    }


    public static final RegistryObject<EvilGrindstoneItem> EVIL_GRINDSTONE = register("evil_grindstone",
        () -> new EvilGrindstoneItem(new Item.Properties().stacksTo(1).tab(NFFGirlsTabs.MAIN_TAB))
            .descTranslatable("desc.nffgirlsgaia.evil_grindstone").cast());
    public static final RegistryObject<Item> INFERIOR_FAN = register("inferior_fan",
        () -> new Item(new Item.Properties().stacksTo(1).tab(NFFGirlsTabs.MAIN_TAB)));
    public static final RegistryObject<Item> WOODEN_STAFF = register("wooden_staff",
        () -> new Item(new Item.Properties().stacksTo(1).tab(NFFGirlsTabs.MAIN_TAB)));

    public static final RegistryObject<Item> MOB_DICT = registerModDependent("mob_dictionary", "citadel",
        () -> new CitadelBasedMobDictionaryItem(new Item.Properties().tab(NFFGirlsTabs.MAIN_TAB),
            new ResourceLocation("nffgirlsgaia","book/mob_dictionary/root.json"),
            "dict.nffgirlsgaia.title", "nffgirlsgaia:book/mob_dictionary/"), NFFGirlsTabs.MAIN_TAB);


    /*@SubscribeEvent
    public static void putTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab().equals(NFFGirlsTabs.TAB.get()))
            for (var item: ITEMS.getEntries()) {
                if (!NO_TAB.contains(item)) event.accept(item);
            }
    }*/

}
