package com.clefal.teams.mixin.mine_and_slash;

import com.mojang.brigadier.CommandDispatcher;
import com.robertx22.mine_and_slash.vanilla_mc.commands.CommandRefs;
import com.robertx22.mine_and_slash.vanilla_mc.commands.TeamCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.commands.Commands.literal;

@Mixin(value = TeamCommand.class)
public abstract class TeamCommandMixin {

    @Inject(method = "register", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void cancelRegister(CommandDispatcher<CommandSourceStack> commandDispatcher, CallbackInfo ci) {
        commandDispatcher.register(
                literal(CommandRefs.ID).requires(x -> true)
                        .then(literal("teams").requires(e -> true)
                                .then(literal("help").executes(commandContext -> {
                                    ServerPlayer player = commandContext.getSource()
                                            .getPlayerOrException();
                                    player.sendSystemMessage(Component.translatable("teams.compat.mns_command"));
                                    return 0;
                                }))
                                .then(Commands.literal("list_members").executes((x) -> {
                                    Player player = x.getSource().getPlayerOrException();
                                    TeamCommand.listMembers(player);
                                    return 0;
                                }))));
        ci.cancel();
    }

}
