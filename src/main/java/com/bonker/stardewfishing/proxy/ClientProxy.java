package com.bonker.stardewfishing.proxy;

import com.bonker.stardewfishing.client.FishingScreen;
import com.bonker.stardewfishing.common.networking.C2SCompleteMinigamePacket;
import com.bonker.stardewfishing.common.networking.S2CStartMinigamePacket;
import com.bonker.stardewfishing.common.networking.SFNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class ClientProxy {
    /**
     * Client-side setting to skip the fishing minigame.
     * When true, fishing automatically succeeds with perfect accuracy.
     */
    private static boolean skipMinigame = false;

    /**
     * Called when the server wants to start a fishing minigame.
     * 
     * If skip-game is enabled, immediately sends a success packet back to the server
     * with perfect accuracy (1.0) and collects any available treasure chest.
     * 
     * If skip-game is disabled, opens the normal fishing minigame screen.
     */
    public static void openFishingScreen(S2CStartMinigamePacket packet) {
        if (skipMinigame) {
            // Skip the minigame and immediately send success with perfect accuracy
            SFNetworking.sendToServer(new C2SCompleteMinigamePacket(true, 1.0, packet.treasureChest()));
        } else {
            Minecraft.getInstance().setScreen(new FishingScreen(packet));
        }
    }

    public static boolean isShiftDown() {
        return Screen.hasShiftDown();
    }

    /**
     * Sets the skip-game state. Called by the client command.
     * 
     * @param skip true to enable skip-game, false to disable
     */
    public static void setSkipMinigame(boolean skip) {
        skipMinigame = skip;
    }

    /**
     * @return true if skip-game is currently enabled
     */
    public static boolean isSkipMinigame() {
        return skipMinigame;
    }
}
