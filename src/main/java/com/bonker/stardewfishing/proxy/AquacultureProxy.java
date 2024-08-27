package com.bonker.stardewfishing.proxy;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.inventory.container.TackleBoxContainer;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;

public class AquacultureProxy {
    public static void overrideStackingBehavior(ItemStackedOnOtherEvent event) {
        if (event.getPlayer().containerMenu instanceof TackleBoxContainer && event.getSlot().index == 4) {
            ItemStack carried = event.getCarriedSlotAccess().get();
            ItemStack slotItem = event.getCarriedItem();

            if (carried.is(AquacultureAPI.Tags.BOBBER) && !(carried.getItem() instanceof DyeableLeatherItem)) {
                if (slotItem.isEmpty()) {
                    event.getSlot().setByPlayer(carried.copyWithCount(1));
                    carried.shrink(1);
                    event.setCanceled(true);
                } else if (carried.getCount() == 1) {
                    event.getSlot().setByPlayer(carried.copyWithCount(1));
                    event.getCarriedSlotAccess().set(slotItem);
                    event.setCanceled(true);
                }
            }
        }
    }
}
