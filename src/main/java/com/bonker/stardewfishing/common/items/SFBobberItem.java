package com.bonker.stardewfishing.common.items;

import com.bonker.stardewfishing.proxy.TideProxy;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
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
        return List.of(Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
    }

    // called by tide
    public ResourceLocation getTextureLocation() {
        return TideProxy.DEFAULT_BOBBER_TEXTURE;
    }

    // called by tide
    public Component getTranslation() {
        return getDescription();
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
