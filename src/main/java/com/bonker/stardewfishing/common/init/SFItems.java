package com.bonker.stardewfishing.common.init;

import com.bonker.stardewfishing.StardewFishing;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

public class SFItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, StardewFishing.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, StardewFishing.MODID);

    public static final RegistryObject<Item> TRAP_BOBBER = ITEMS.register("trap_bobber", () -> new BobberItem(new Item.Properties().durability(64)));
    public static final RegistryObject<Item> CORK_BOBBER = ITEMS.register("cork_bobber", () -> new BobberItem(new Item.Properties().durability(64)));
    public static final RegistryObject<Item> SONAR_BOBBER = ITEMS.register("sonar_bobber", () -> new BobberItem(new Item.Properties().durability(64)));
    public static final RegistryObject<Item> TREASURE_BOBBER = ITEMS.register("treasure_bobber", () -> new BobberItem(new Item.Properties().durability(64)));
    @Nullable
    public static final RegistryObject<Item> QUALITY_BOBBER = StardewFishing.QUALITY_FOOD_INSTALLED ?
            ITEMS.register("quality_bobber", () -> new BobberItem(new Item.Properties().durability(64)))
            : null;

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("items", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.stardewFishing"))
            .icon(() -> new ItemStack(TRAP_BOBBER.get()))
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(TRAP_BOBBER.get());
                pOutput.accept(CORK_BOBBER.get());
                pOutput.accept(SONAR_BOBBER.get());
                pOutput.accept(TREASURE_BOBBER.get());
                if (QUALITY_BOBBER != null) {
                    pOutput.accept(QUALITY_BOBBER.get());
                }
            })
            .build());

    public static class BobberItem extends Item implements DyeableLeatherItem {
        private Component tooltip;

        public BobberItem(Properties pProperties) {
            super(pProperties);
        }

        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
            if (tooltip == null) {
                tooltip = Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY);
            }
            pTooltipComponents.add(tooltip);
        }
    }
}
