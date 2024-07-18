package com.bonker.stardewfishing.common;

import com.bonker.stardewfishing.StardewFishing;
import com.bonker.stardewfishing.server.FishBehaviorReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonEvents {
    @Mod.EventBusSubscriber(modid = StardewFishing.MODID)
    public static class ForgeBus {
        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof FishingHook) {
                FishingHookLogic.attachCap(event);
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
            if (event.player.fishing != null) {
//                FishingHook hook = event.player.fishing;
//                FishingHookLogic.getStoredRewards(hook).ifPresent(rewards -> {
//                    if (rewards.isEmpty()) {
//                        hook.nibble = -1;
//                        hook.timeUntilHooked = -1;
//                        hook.timeUntilLured = -1;
//                    }
//                });
            }
        }

        @SubscribeEvent
        public static void onAddReloadListeners(final AddReloadListenerEvent event) {
            event.addListener(FishBehaviorReloadListener.create());
        }

        @SubscribeEvent
        public static void onEntityJoinLevel(final EntityJoinLevelEvent event) {
            if (!event.getLevel().isClientSide && event.getEntity().getType() == EntityType.FISHING_BOBBER) {
//                FishingHook hook = (FishingHook) event.getEntity();
//                event.setCanceled(true);
//                event.getLevel().addFreshEntity(new StardewFishingHook(hook.getPlayerOwner(), event.getLevel(), hook.luck, hook.lureSpeed));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = StardewFishing.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void onCommonSetup(final FMLCommonSetupEvent event) {
            event.enqueueWork(() ->
                    StardewFishing.QUALITY_FOOD_INSTALLED = ModList.get().isLoaded("quality_food")
            );
        }
    }
}
