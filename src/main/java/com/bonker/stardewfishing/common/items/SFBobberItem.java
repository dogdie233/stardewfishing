package com.bonker.stardewfishing.common.items;

import com.bonker.stardewfishing.StardewFishing;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SFBobberItem extends Item implements DyeableLeatherItem {
    private List<Component> tooltip;

    public SFBobberItem(Properties pProperties) {
        super(pProperties);
    }

    protected List<Component> makeTooltip() {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable(getDescriptionId() + ".tooltip").withStyle(StardewFishing.LIGHTER_COLOR));
        return tooltip;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (tooltip == null) {
            tooltip = makeTooltip();
        }
        pTooltipComponents.addAll(tooltip);
    }
}
