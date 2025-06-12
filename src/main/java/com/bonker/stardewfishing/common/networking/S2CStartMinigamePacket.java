package com.bonker.stardewfishing.common.networking;

import com.bonker.stardewfishing.common.FishBehavior;
import com.bonker.stardewfishing.proxy.ClientProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record S2CStartMinigamePacket(FishBehavior behavior, ItemStack fish, boolean treasureChest, boolean goldenChest, float lineStrength, int barSize, boolean lava) {
    public S2CStartMinigamePacket(FriendlyByteBuf buf) {
        this(new FishBehavior(buf), buf.readItem(), buf.readBoolean(), buf.readBoolean(), buf.readFloat(), buf.readShort(), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        behavior.writeToBuffer(buf);
        buf.writeItem(fish);
        buf.writeBoolean(treasureChest);
        buf.writeBoolean(goldenChest);
        buf.writeFloat(lineStrength);
        buf.writeShort(barSize);
        buf.writeBoolean(lava);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> ClientProxy.openFishingScreen(this));
    }
}
