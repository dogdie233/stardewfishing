package com.bonker.stardewfishing.server;

import com.bonker.stardewfishing.common.items.AttributeAttachmentItem;
import com.bonker.stardewfishing.proxy.ItemUtils;
import com.google.common.collect.Multimap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Pair;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class AttributeCache {
    public static final Map<Player, Pair<ItemStack, Map<Attribute, Double>>> CACHE_MAP = new HashMap<>();

    public static double getAttribute(Player player, Attribute attribute) {
        if (CACHE_MAP.containsKey(player)) {
            return CACHE_MAP.get(player).getB().getOrDefault(attribute, attribute.getDefaultValue());
        } else {
            return attribute.getDefaultValue();
        }
    }

    public static void add(Player player) {
        ItemStack fishingRod = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!ItemUtils.isFishingRod(fishingRod)) {
            fishingRod = player.getItemInHand(InteractionHand.OFF_HAND);
        }

        ItemStack bobber = ItemUtils.getBobber(fishingRod);
        if (bobber.getItem() instanceof AttributeAttachmentItem) {
            add(player, fishingRod, AttributeAttachmentItem.getAttachmentModifiers(bobber));
        } else {
            add(player, fishingRod, null);
        }
    }

    private static void add(Player player, ItemStack fishingRod, @Nullable Multimap<Attribute, AttributeModifier> bobberModifiers) {
        AttributeMap playerAttributes = player.getAttributes();

        if (bobberModifiers != null) {
            playerAttributes.addTransientAttributeModifiers(bobberModifiers);
        }

        Map<Attribute, Double> cache = new HashMap<>();
        playerAttributes.attributes.forEach((attribute, inst) -> cache.put(attribute, inst.getValue()));
        CACHE_MAP.put(player, new Pair<>(fishingRod, cache));

        if (bobberModifiers != null) {
            playerAttributes.removeAttributeModifiers(bobberModifiers);
        }
    }

    public static void remove(Player player) {
        CACHE_MAP.remove(player);
    }
}
