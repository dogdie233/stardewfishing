package com.bonker.stardewfishing.proxy;

import com.bonker.stardewfishing.client.FishingScreen;
import com.bonker.stardewfishing.common.FishBehavior;
import net.minecraft.client.Minecraft;

public class ClientProxy {
    public static void openFishingScreen(FishBehavior behavior) {
        Minecraft.getInstance().setScreen(new FishingScreen(behavior));
    }
}
