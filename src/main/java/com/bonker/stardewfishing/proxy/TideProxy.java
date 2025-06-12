package com.bonker.stardewfishing.proxy;

import com.bonker.stardewfishing.common.FishingHookLogic;
import com.li64.tide.data.TideTags;
import com.li64.tide.data.rods.CustomRodManager;
import com.li64.tide.registries.items.TideFishingRodItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class TideProxy {
    public static void damageEquippedBobber(ItemStack fishingRod, ServerPlayer player) {
        if (!CustomRodManager.hasBobber(fishingRod)) {
            return;
        }
        FishingHookLogic.damageBobber(getBobber(fishingRod), player)
                .ifPresent(b -> CustomRodManager.setBobber(fishingRod, b));
    }

    public static ItemStack getBobber(ItemStack fishingRod) {
        return CustomRodManager.hasBobber(fishingRod) ? CustomRodManager.getBobber(fishingRod) : ItemStack.EMPTY;
    }

    public static boolean isTideRod(ItemStack fishingRod) {
        return fishingRod.getItem() instanceof TideFishingRodItem;
    }

    public static boolean isTideBobber(ItemStack stack) {
        return stack.is(TideTags.Items.BOBBERS);
    }

    public static void setBobber(ItemStack fishingRod, ItemStack bobber) {
        CustomRodManager.setBobber(fishingRod, bobber);
    }
}
