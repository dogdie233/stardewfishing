package com.bonker.stardewfishing.proxy;

import com.bonker.stardewfishing.common.FishingHookLogic;
import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class AquacultureProxy {
    public static void damageEquippedBobber(ItemStack fishingRod, ServerPlayer player) {
        ItemStackHandler handler = AquaFishingRodItem.getHandler(fishingRod);
        FishingHookLogic.damageBobber(handler.getStackInSlot(3), player)
                .ifPresent(b -> handler.setStackInSlot(3, b));
    }

    public static ItemStack getBobber(ItemStack fishingRod) {
        return AquaFishingRodItem.getBobber(fishingRod);
    }

    public static boolean isAquaRod(ItemStack fishingRod) {
        return fishingRod.getItem() instanceof AquaFishingRodItem;
    }
}
