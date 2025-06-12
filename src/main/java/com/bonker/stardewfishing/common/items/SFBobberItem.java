package com.bonker.stardewfishing.common.items;

import com.bonker.stardewfishing.StardewFishing;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SFBobberItem extends Item implements DyeableLeatherItem, AttributeAttachmentItem {
    private final Multimap<Attribute, AttributeModifier> modifiers;
    @Nullable
    private Consumer<Multimap<Attribute, AttributeModifier>> modifiersCreator;
    private List<Component> tooltip;

    public SFBobberItem(Properties pProperties) {
        super(pProperties);
        modifiers = HashMultimap.create();
    }

    public SFBobberItem(Properties pProperties, Consumer<Multimap<Attribute, AttributeModifier>> consumer) {
        this(pProperties);
        modifiersCreator = consumer;
    }

    protected List<Component> makeTooltip() {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable(getDescriptionId() + ".tooltip").withStyle(StardewFishing.LIGHT_COLOR));
        return tooltip;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttachmentModifiers(ItemStack stack) {
        if (modifiersCreator != null) {
            modifiersCreator.accept(modifiers);
            modifiersCreator = null;
        }

        return modifiers;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (tooltip == null) {
            tooltip = makeTooltip();
        }
        pTooltipComponents.addAll(tooltip);

        AttributeAttachmentItem.appendAttributesTooltip(pStack, pTooltipComponents, "item.modifiers.stardew_fishing.fishing_rod");
    }
}
