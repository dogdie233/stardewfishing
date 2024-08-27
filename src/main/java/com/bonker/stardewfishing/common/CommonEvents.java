package com.bonker.stardewfishing.common;

import com.bonker.stardewfishing.StardewFishing;
import com.bonker.stardewfishing.proxy.AquacultureProxy;
import com.bonker.stardewfishing.server.FishBehaviorReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonEvents {
    @Mod.EventBusSubscriber(modid = StardewFishing.MODID)
    public static class ForgeBus {
        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(final AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof FishingHook) {
                FishingHookLogic.attachCap(event);
            }
        }

        @SubscribeEvent
        public static void onAddReloadListeners(final AddReloadListenerEvent event) {
            event.addListener(FishBehaviorReloadListener.create());
        }

        @SubscribeEvent
        public static void onItemStackedOnOther(final ItemStackedOnOtherEvent event) {
            if (StardewFishing.AQUACULTURE_INSTALLED) {
                AquacultureProxy.overrideStackingBehavior(event);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = StardewFishing.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void onCommonSetup(final FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                StardewFishing.QUALITY_FOOD_INSTALLED = ModList.get().isLoaded("quality_food");
                StardewFishing.AQUACULTURE_INSTALLED = ModList.get().isLoaded("aquaculture");
            });
        }
    }
}
