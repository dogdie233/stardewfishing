package com.bonker.stardewfishing.common.init;

import com.bonker.stardewfishing.StardewFishing;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SFItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, StardewFishing.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, StardewFishing.MODID);

    public static final RegistryObject<Item> TRAP_BOBBER = ITEMS.register("trap_bobber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CORK_BOBBER = ITEMS.register("cork_bobber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SONAR_BOBBER = ITEMS.register("sonar_bobber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TREASURE_BOBBER = ITEMS.register("treasure_bobber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> QUALITY_BOBBER = ITEMS.register("quality_bobber", () -> new Item(new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("items", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.stardewFishing"))
            .icon(() -> new ItemStack(Items.FISHING_ROD))
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(TRAP_BOBBER.get());
                pOutput.accept(CORK_BOBBER.get());
                pOutput.accept(SONAR_BOBBER.get());
                pOutput.accept(TREASURE_BOBBER.get());
                if (StardewFishing.QUALITY_FOOD_INSTALLED) {
                    pOutput.accept(QUALITY_BOBBER.get());
                }
            })
            .build());
}
