package com.bonker.stardewfishing.proxy;

import com.li64.tide.data.rods.CustomRodManager;
import net.minecraft.world.item.ItemStack;

public class TideProxy {
    public static ItemStack getBobber(ItemStack fishingRod) {
        return CustomRodManager.getBobber(fishingRod);
    }
}
