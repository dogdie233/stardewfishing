package com.bonker.stardewfishing.proxy;

import com.bonker.stardewfishing.StardewFishing;
import com.li64.tide.data.rods.CustomRodManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            return stack.is(StardewFishing.BOBBERS);
        }
    }

    public static void setBobber(ItemStack fishingRod, ItemStack bobber) {
        if (StardewFishing.AQUACULTURE_INSTALLED && AquacultureProxy.isAquaRod(fishingRod)) {
            AquacultureProxy.setBobber(fishingRod, bobber);
        } else if (StardewFishing.TIDE_INSTALLED && TideProxy.isTideRod(fishingRod)) {
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

    public static FishingHook spawnHook(ServerPlayer player, ItemStack fishingRod, Vec3 pos) {
        if (StardewFishing.AQUACULTURE_INSTALLED && AquacultureProxy.isAquaRod(fishingRod)) {
            return AquacultureProxy.spawnHook(player, fishingRod, pos);
        } else if (StardewFishing.TIDE_INSTALLED && TideProxy.isTideRod(fishingRod)) {
            return TideProxy.spawnHook(player, fishingRod, pos);
        } else {
            FishingHook hook = new FishingHook(player, player.level(), 0, 0) {
                @Override
                public void tick() {
                    baseTick();
                }
            };
            hook.setPos(pos);
            player.level().addFreshEntity(hook);
            return hook;
        }
    }

    public static List<ItemStack> getAllModifierItems(ItemStack fishingRod) {
        if (StardewFishing.AQUACULTURE_INSTALLED && AquacultureProxy.isAquaRod(fishingRod)) {
            return AquacultureProxy.getAllModifierItems(fishingRod);
        } else if (StardewFishing.TIDE_INSTALLED && TideProxy.isTideRod(fishingRod)) {
            return TideProxy.getAllModifierItems(fishingRod);
        } else {
            List<ItemStack> modifiers = new ArrayList<>();
            modifiers.add(fishingRod);
            ItemStack bobber = getBobberNBT(fishingRod);
            if (!bobber.isEmpty()) {
                modifiers.add(bobber);
            }
            return modifiers;
        }
    }

    public static int getLuck(FishingHook hook) {
        if (StardewFishing.TIDE_INSTALLED && TideProxy.isTideHookEntity(hook)) {
            return TideProxy.getLuck(hook);
        } else {
            return hook.luck;
        }
    }
}
