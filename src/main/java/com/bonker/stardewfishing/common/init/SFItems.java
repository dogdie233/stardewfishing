package com.bonker.stardewfishing.common.init;

import com.bonker.stardewfishing.SFConfig;
import com.bonker.stardewfishing.StardewFishing;
import com.bonker.stardewfishing.common.items.SFBobberItem;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class SFItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, StardewFishing.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, StardewFishing.MODID);

    public static final RegistryObject<Item> TRAP_BOBBER = ITEMS.register("trap_bobber", () -> new SFBobberItem(new Item.Properties().durability(64)));

    public static final RegistryObject<Item> CORK_BOBBER = ITEMS.register("cork_bobber", () -> new SFBobberItem(new Item.Properties().durability(64)));

    public static final RegistryObject<Item> SONAR_BOBBER = ITEMS.register("sonar_bobber", () -> new SFBobberItem(new Item.Properties().durability(64)));

    public static final RegistryObject<Item> TREASURE_BOBBER = ITEMS.register("treasure_bobber", () -> new SFBobberItem(new Item.Properties().durability(64)));

    public static final RegistryObject<Item> QUALITY_BOBBER = ITEMS.register("quality_bobber", () -> new SFBobberItem(new Item.Properties().durability(64)) {
        @Override
        protected List<Component> makeTooltip() {
            List<Component> tooltip = super.makeTooltip();
            if (StardewFishing.QUALITY_FOOD_INSTALLED) {
                tooltip.add(1, Component.translatable(getDescriptionId() + ".quality_food_tooltip").withStyle(StardewFishing.LIGHTER_COLOR));
            }
            return tooltip;
        }
    });

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("items", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.stardewFishing"))
            .icon(() -> new ItemStack(TRAP_BOBBER.get()))
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(TRAP_BOBBER.get());
                pOutput.accept(CORK_BOBBER.get());
                pOutput.accept(SONAR_BOBBER.get());
                pOutput.accept(TREASURE_BOBBER.get());
                pOutput.accept(QUALITY_BOBBER.get());
            })
            .build());
}
