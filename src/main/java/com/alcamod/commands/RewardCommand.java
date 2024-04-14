package com.alcamod.commands;

import com.alcamod.client.gui.RewardScreen;
import com.alcamod.network.RewardScreenPacketHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RewardCommand {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("reward")
                .requires(cs -> cs.hasPermission(2)) // Replace '2' with the desired permission level
                .executes(context -> {
                    ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
                    RewardScreenPacketHandler.sendOpenRewardScreenPacket(serverPlayer);
                    return 1;
                }));
    }
}
