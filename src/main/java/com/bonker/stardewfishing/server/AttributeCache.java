package com.bonker.stardewfishing.server;

import com.bonker.stardewfishing.common.items.AttributeAttachmentItem;
import com.bonker.stardewfishing.proxy.BobberGetter;
import com.google.common.collect.Multimap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Pair;

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
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!(stack.getItem() instanceof FishingRodItem)) {
            stack = player.getItemInHand(InteractionHand.OFF_HAND);
        }

        ItemStack bobber = BobberGetter.getBobber(stack);

        if (bobber.getItem() instanceof AttributeAttachmentItem) {
            Multimap<Attribute, AttributeModifier> itemModifiers = AttributeAttachmentItem.getAttachmentModifiers(bobber);
            AttributeMap playerAttributes = player.getAttributes();

            playerAttributes.addTransientAttributeModifiers(itemModifiers);

            Map<Attribute, Double> cache = new HashMap<>();
            playerAttributes.attributes.forEach((attribute, inst) -> cache.put(attribute, inst.getValue()));
            CACHE_MAP.put(player, new Pair<>(stack, cache));

            playerAttributes.removeAttributeModifiers(itemModifiers);
        }
    }

    public static void remove(Player player) {
        CACHE_MAP.remove(player);
    }
}
