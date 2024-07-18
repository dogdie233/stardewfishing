package com.bonker.stardewfishing.common.networking;

import com.bonker.stardewfishing.common.FishingHookLogic;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.function.Supplier;

public record C2SCompleteMinigamePacket(boolean success, double accuracy) {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static C2SCompleteMinigamePacket decode(FriendlyByteBuf buf) {
        boolean success = buf.readBoolean();
        return new C2SCompleteMinigamePacket(success, success ? buf.readDouble() : -1);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(success);
        if (success) buf.writeDouble(accuracy);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        ServerPlayer player = contextSupplier.get().getSender();
        if (player == null) {
            return;
        }

        FishingHook hook = player.fishing;
        if (hook == null || FishingHookLogic.getStoredRewards(hook).isEmpty()) {
            LOGGER.warn("{} tried to complete a fishing minigame that doesn't exist", player.getScoreboardName());
            return;
        }

        contextSupplier.get().enqueueWork(() -> {
            InteractionHand hand = FishingHookLogic.getRodHand(player);
            if (hand == null) {
                FishingHookLogic.endMinigame(player, false, 0);
                LOGGER.warn("{} tried to complete a fishing minigame without a fishing rod", player.getScoreboardName());
            } else {
                FishingHookLogic.endMinigame(player, success, accuracy);
                player.getItemInHand(hand).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
        });
    }
}
