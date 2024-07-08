package com.bonker.stardewfishing.common;

import com.bonker.stardewfishing.StardewFishing;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class SFNetworking {
    private static final String PROTOCOL_VERSION = "1";

    private static SimpleChannel CHANNEL;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public void register() {
        CHANNEL = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(StardewFishing.MODID, "packets"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .simpleChannel();


    }
}
