package com.bonker.stardewfishing.proxy;

import com.bonker.stardewfishing.client.FishingScreen;
import com.bonker.stardewfishing.common.networking.C2SCompleteMinigamePacket;
import com.bonker.stardewfishing.common.networking.S2CStartMinigamePacket;
import com.bonker.stardewfishing.common.networking.SFNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class ClientProxy {
    private static boolean skipMinigame = false;

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

    public static void setSkipMinigame(boolean skip) {
        skipMinigame = skip;
    }

    public static boolean isSkipMinigame() {
        return skipMinigame;
    }
}
