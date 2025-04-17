package com.bonker.stardewfishing.common.items;

import com.bonker.stardewfishing.common.init.SFAttributes;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;

public interface AttributeAttachmentItem {
    ImmutableMultimap<Attribute, AttributeModifier> EMPTY = ImmutableMultimap.of();
    String SLOT_ATTACHMENT = "attachment";

    default Multimap<Attribute, AttributeModifier> getDefaultAttachmentModifiers(ItemStack stack) {
        return EMPTY;
    }

    static Multimap<Attribute, AttributeModifier> getAttachmentModifiers(ItemStack stack) {
        if (!(stack.getItem() instanceof AttributeAttachmentItem attachmentItem)) {
            return EMPTY;
        }

        if (!stack.hasTag() || !stack.getOrCreateTag().contains("AttachmentModifiers", Tag.TAG_LIST)) {
            return attachmentItem.getDefaultAttachmentModifiers(stack);
        }

        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        ListTag slots = stack.getOrCreateTag().getList("AttachmentModifiers", Tag.TAG_COMPOUND);
        for (int i = 0; i < slots.size(); ++i) {
            CompoundTag slot = slots.getCompound(i);
            if (slot.contains("Slot", Tag.TAG_STRING) && !slot.getString("Slot").equals(SLOT_ATTACHMENT)) continue;

            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryParse(slot.getString("AttributeName")));
            if (attribute == null) continue;

            AttributeModifier modifier = AttributeModifier.load(slot);
            if (modifier == null || modifier.getId().getLeastSignificantBits() == 0 || modifier.getId().getMostSignificantBits() == 0) continue;

            modifiers.put(attribute, modifier);
        }

        return modifiers;
    }

    static void appendAttributesTooltip(ItemStack pStack, List<Component> pTooltipComponents, String attachmentMessageKey) {
        // replicate vanilla attribute tooltip
        if (!pStack.hasTag() || (pStack.getOrCreateTag().getInt("HideFlags") & ItemStack.TooltipPart.MODIFIERS.getMask()) == 0) {
            Multimap<Attribute, AttributeModifier> multimap = getAttachmentModifiers(pStack);
            if (!multimap.isEmpty()) {
                pTooltipComponents.add(CommonComponents.EMPTY);
                pTooltipComponents.add(Component.translatable(attachmentMessageKey).withStyle(ChatFormatting.GRAY));

                for (Map.Entry<Attribute, AttributeModifier> entry : multimap.entries()) {
                    Attribute attribute = entry.getKey();
                    AttributeModifier modifier = entry.getValue();
                    double amount = modifier.getAmount();
                    AttributeModifier.Operation operation = modifier.getOperation();

                    // hacky fix: make treasure bobber tooltip show percent even though it is addition
                    if (attribute == SFAttributes.TREASURE_CHANCE_BONUS.get()) {
                        operation = AttributeModifier.Operation.MULTIPLY_TOTAL;
                    }

                    double display = amount;
                    if (operation == AttributeModifier.Operation.MULTIPLY_BASE || operation == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                        display *= 100;
                    }

                    if (amount > 0) {
                        pTooltipComponents.add(Component.translatable("attribute.modifier.plus." + operation.toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(display), Component.translatable(attribute.getDescriptionId())).withStyle(ChatFormatting.BLUE));
                    } else if (amount < 0) {
                        display *= -1;
                        pTooltipComponents.add(Component.translatable("attribute.modifier.take." + operation.toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(display), Component.translatable(attribute.getDescriptionId())).withStyle(ChatFormatting.RED));
                    }
                }
            }
        }
    }
}
