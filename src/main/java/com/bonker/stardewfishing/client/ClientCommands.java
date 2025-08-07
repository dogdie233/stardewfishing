package com.bonker.stardewfishing.client;

import com.bonker.stardewfishing.proxy.ClientProxy;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientCommands {
    @SubscribeEvent
    public static void registerClientCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        dispatcher.register(Commands.literal("stardewfishing")
                .then(Commands.literal("skip-game")
                        .then(Commands.argument("state", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    builder.suggest("on");
                                    builder.suggest("off");
                                    return builder.buildFuture();
                                })
                                .executes(ClientCommands::setSkipGame))));
    }

    private static int setSkipGame(CommandContext<CommandSourceStack> context) {
        String state = StringArgumentType.getString(context, "state");
        
        switch (state.toLowerCase()) {
            case "on":
                ClientProxy.setSkipMinigame(true);
                context.getSource().sendSuccess(() -> Component.literal("Stardew Fishing minigame skip enabled"), false);
                break;
            case "off":
                ClientProxy.setSkipMinigame(false);
                context.getSource().sendSuccess(() -> Component.literal("Stardew Fishing minigame skip disabled"), false);
                break;
            default:
                context.getSource().sendFailure(Component.literal("Invalid argument. Use 'on' or 'off'"));
                return 0;
        }
        
        return 1;
    }
}