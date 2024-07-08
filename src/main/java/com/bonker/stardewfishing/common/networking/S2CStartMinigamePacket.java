package com.bonker.stardewfishing.common.networking;

import com.bonker.stardewfishing.common.FishDifficulty;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record S2CFishingMinigamePacket(FishDifficulty fishDifficulty) {
    public void encode(FriendlyByteBuf buf) {
        fishDifficulty.writeToBuffer(buf);
    }

    public S2CFishingMinigamePacket(FriendlyByteBuf buf) {
        this(FishDifficulty.readFromBuffer(buf));
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {

    }
}
