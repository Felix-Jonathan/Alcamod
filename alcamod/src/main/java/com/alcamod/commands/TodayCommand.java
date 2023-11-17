package com.alcamod.commands;
import com.alcamod.gui.DailyGui;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TodayCommand {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("aujourdhui").executes(TodayCommand::execute));
    }


    private static int execute(CommandContext<CommandSource> context) {
        try {
            context.getSource().getPlayerOrException();
            DailyGui.openGUI();
        } catch (CommandSyntaxException e) {
            context.getSource().sendFailure(new TranslationTextComponent("commands.error.player_required"));
            return 0;
        }
        return 1;
    }


}
