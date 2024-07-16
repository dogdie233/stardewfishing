package com.bonker.stardewfishing.common.networking;

import com.bonker.stardewfishing.client.ClientEvents;
import com.bonker.stardewfishing.common.FishBehavior;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record S2CStartMinigamePacket(FishBehavior behavior) {
    public S2CStartMinigamePacket(FriendlyByteBuf buf) {
        this(new FishBehavior(buf));
    }

    public void encode(FriendlyByteBuf buf) {
        behavior.writeToBuffer(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> ClientEvents.openFishingScreen(behavior));
    }
}
