package com.bonker.stardewfishing.common;

import com.bonker.stardewfishing.StardewFishing;
import com.bonker.stardewfishing.client.RodTooltipHandler;
import com.bonker.stardewfishing.common.init.SFAttributes;
import com.bonker.stardewfishing.proxy.ClientProxy;
import com.bonker.stardewfishing.proxy.ItemUtils;
import com.bonker.stardewfishing.server.FishBehaviorReloadListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class CommonEvents {
    @Mod.EventBusSubscriber(modid = StardewFishing.MODID)
    public static class ForgeBus {
        @SubscribeEvent
        public static void onItemStackedOnOther(final ItemStackedOnOtherEvent event) {
            if (event.getClickAction() != ClickAction.SECONDARY) {
                return;
            }

            ItemStack slotItem = event.getSlot().getItem();
            if (!ItemUtils.isFishingRod(slotItem)) {
                return;
            }

            ItemStack carried = event.getStackedOnItem();
            ItemStack currentBobber = ItemUtils.getBobber(slotItem);

            if (ItemUtils.isBobber(carried)) {
                ItemUtils.setBobber(slotItem, carried.copy());
                event.getCarriedSlotAccess().set(currentBobber.copy());
                event.setCanceled(true);

                if (event.getPlayer().level().isClientSide && FMLEnvironment.dist.isClient()) {
                    RodTooltipHandler.addShake(event.getSlot(), true);
                }
            } else if (!currentBobber.isEmpty() && carried.isEmpty()) {
                ItemUtils.setBobber(slotItem, ItemStack.EMPTY);
                event.getCarriedSlotAccess().set(currentBobber.copy());
                event.setCanceled(true);

                if (event.getPlayer().level().isClientSide && FMLEnvironment.dist.isClient()) {
                    RodTooltipHandler.addShake(event.getSlot(), false);
                }
            }
        }

        @SubscribeEvent
        public static void onTooltip(final ItemTooltipEvent event) {
            if (!StardewFishing.TIDE_INSTALLED && ItemUtils.isFishingRod(event.getItemStack())) {
                ItemStack bobber = ItemUtils.getBobber(event.getItemStack());

                if (bobber.isEmpty()) {
                    event.getToolTip().add(Component.translatable("item.stardew_fishing.no_bobber.tooltip")
                            .withStyle(StardewFishing.DARK_COLOR));
                } else {
                    event.getToolTip().add(Component.translatable("item.stardew_fishing.bobber.tooltip", bobber.getDisplayName().copy().withStyle(StardewFishing.LIGHT_COLOR))
                            .withStyle(StardewFishing.DARK_COLOR));
                }
            }
        }

        @SubscribeEvent
        public static void onAttachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof FishingHook) {
                FishingHookLogic.attachCap(event);
            }
        }

        @SubscribeEvent
        public static void onAddReloadListeners(final AddReloadListenerEvent event) {
            event.addListener(FishBehaviorReloadListener.create());
        }
    }

    @Mod.EventBusSubscriber(modid = StardewFishing.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void onAttributeCreation(final EntityAttributeModificationEvent event) {
            event.add(EntityType.PLAYER, SFAttributes.LINE_STRENGTH.get());
            event.add(EntityType.PLAYER, SFAttributes.BAR_SIZE.get());
            event.add(EntityType.PLAYER, SFAttributes.TREASURE_CHANCE_BONUS.get());
            event.add(EntityType.PLAYER, SFAttributes.EXPERIENCE_MULTIPLIER.get());
        }
    }
}
