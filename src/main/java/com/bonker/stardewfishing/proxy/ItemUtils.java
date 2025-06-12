package com.bonker.stardewfishing.proxy;

import com.bonker.stardewfishing.StardewFishing;
import com.bonker.stardewfishing.common.items.SFBobberItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;

public class ItemUtils {
    public static ItemStack getBobber(ItemStack fishingRod) {
        if (StardewFishing.AQUACULTURE_INSTALLED && AquacultureProxy.isAquaRod(fishingRod)) {
            return AquacultureProxy.getBobber(fishingRod);
        } else if (StardewFishing.TIDE_INSTALLED && TideProxy.isTideRod(fishingRod)) {
            return TideProxy.getBobber(fishingRod);
        } else {
            return getBobberNBT(fishingRod);
        }
    }

    public static boolean isFishingRod(ItemStack stack) {
        if (StardewFishing.AQUACULTURE_INSTALLED && AquacultureProxy.isAquaRod(stack)) {
            return true;
        } else if (StardewFishing.TIDE_INSTALLED && TideProxy.isTideRod(stack)) {
            return true;
        } else {
            return stack.is(StardewFishing.MODIFIABLE_RODS);
        }
    }

    public static boolean isBobber(ItemStack stack) {
        if (StardewFishing.AQUACULTURE_INSTALLED && AquacultureProxy.isAquaBobber(stack)) {
            return true;
        } else if (StardewFishing.TIDE_INSTALLED && TideProxy.isTideBobber(stack)) {
            return true;
        } else {
            return stack.getItem() instanceof SFBobberItem;
        }
    }

    public static void setBobber(ItemStack fishingRod, ItemStack bobber) {
        if (StardewFishing.AQUACULTURE_INSTALLED) {
            AquacultureProxy.setBobber(fishingRod, bobber);
        } else if (StardewFishing.TIDE_INSTALLED) {
            TideProxy.setBobber(fishingRod, bobber);
        } else {
            setBobberNBT(fishingRod, bobber);
        }
    }

    public static void damageBobber(ItemStack fishingRod, ServerPlayer player) {
        if (StardewFishing.AQUACULTURE_INSTALLED && AquacultureProxy.isAquaRod(fishingRod)) {
            AquacultureProxy.damageEquippedBobber(fishingRod, player);
        } else if (StardewFishing.TIDE_INSTALLED && TideProxy.isTideRod(fishingRod)) {
            TideProxy.damageEquippedBobber(fishingRod, player);
        }
    }

    private static ItemStack getBobberNBT(ItemStack fishingRod) {
        CompoundTag nbt = fishingRod.getOrCreateTag();
        if (nbt.contains("modifier", Tag.TAG_COMPOUND)) {
            CompoundTag modifier = nbt.getCompound("modifier");
            if (modifier.contains("bobber", Tag.TAG_COMPOUND)) {
                return ItemStack.of(modifier.getCompound("bobber"));
            }
        }
        return ItemStack.EMPTY;
    }

    private static void setBobberNBT(ItemStack fishingRod, ItemStack bobber) {
        CompoundTag nbt = fishingRod.getOrCreateTag();
        CompoundTag modifier;
        if (nbt.contains("modifier", Tag.TAG_COMPOUND)) {
            modifier = nbt.getCompound("modifier");
        } else {
            modifier = new CompoundTag();
        }

        modifier.put("bobber", bobber.save(new CompoundTag()));
        nbt.put("modifier", modifier);
    }
}
