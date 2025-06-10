package com.bonker.stardewfishing.proxy;

import com.bonker.stardewfishing.common.FishingHookLogic;
import com.li64.tide.data.rods.CustomRodManager;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import com.li64.tide.registries.items.TideFishingRodItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class TideProxy {
    public static final ResourceLocation DEFAULT_BOBBER_TEXTURE = ResourceLocation.fromNamespaceAndPath("tide", "textures/item/white_fishing_bobber.png");

    public static void damageEquippedBobber(ItemStack fishingRod, ServerPlayer player) {
        if (!CustomRodManager.hasBobber(fishingRod)) {
            return;
        }
        FishingHookLogic.damageBobber(getBobber(fishingRod), player)
                .ifPresent(b -> CustomRodManager.setBobber(fishingRod, b));
    }

    public static ItemStack getBobber(ItemStack fishingRod) {
        return CustomRodManager.getBobber(fishingRod);
    }

    public static boolean isTideRod(ItemStack fishingRod) {
        return fishingRod.getItem() instanceof TideFishingRodItem;
    }

    public static boolean isTideHook(Entity entity) {
        return entity instanceof TideFishingHook;
    }

    @Nullable
    public static Player getTideHookOwner(Entity entity) {
        return ((TideFishingHook) entity).getPlayerOwner();
    }
}
