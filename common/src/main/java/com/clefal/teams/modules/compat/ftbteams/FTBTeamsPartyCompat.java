package com.clefal.teams.modules.compat.ftbteams;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.event.server.*;
import com.clefal.teams.server.ATServerTeam;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.data.PartyTeam;
import dev.ftb.mods.ftbteams.data.TeamManagerImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FTBTeamsPartyCompat {
    @SubscribeEvent
    public void onPlayerOnline(ServerOnPlayerOnlineEvent event){

    }

    @SubscribeEvent
    public void onCreateTeam(ServerCreateTeamEvent event) {
        ServerPlayer player = event.player;
        if (hasNoParty(player)) {
            try {
                TeamManagerImpl.INSTANCE.createParty(player, event.teamReadableName);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static boolean hasNoParty(ServerPlayer player) {
        if (FTBTeamsAPI.api().getManager().getTeamForPlayerID(player.getUUID()).map((team) -> !team.isPartyTeam()).orElse(false)){
            System.out.println("has no party team!");
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onJoinTeam(ServerJoinTeamEvent event) {
        ServerPlayer joiner = event.joiner;
        ATServerTeam team = event.team;
        team.getOnlinePlayers()
                .map(FTBTeamsPartyCompat::getTeamForPlayer)
                .head()
                .filter(x -> x instanceof PartyTeam)
                .ifPresent(x -> {
                    PartyTeam x1 = (PartyTeam) x;
                    if (hasNoParty(joiner)) {
                        try {
                            x1.join(joiner);
                        } catch (CommandSyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

    }

    private static Optional<Team> getTeamForPlayer(ServerPlayer x) {
        return FTBTeamsAPI.api().getManager().getTeamForPlayer(x);
    }

    @SubscribeEvent
    public void onLeaveTeam(ServerPlayerLeaveEvent event) {
        ServerPlayer leaver = event.leaver;
        System.out.println(hasNoParty(leaver));
        getTeamForPlayer(leaver)
                .filter(x -> !hasNoParty(leaver))
                .filter(x -> x instanceof PartyTeam)
                .ifPresent(x -> {
                    PartyTeam x1 = (PartyTeam) x;
                    UUID id = leaver.getUUID();
                    if (x1.isOwner(id) && x1.getMembers().size() > 1){
                        ATServerTeam leavedTeam = event.leavedTeam;
                        CommandSourceStack commandSourceStack = new CommandSourceStack(null, null, null, leaver.serverLevel(), 0, "", Component.literal(""), leaver.getServer(), event.leaver);
                        try {
                            x1.transferOwnership(commandSourceStack, leavedTeam.getOnlinePlayers().find(player -> player.getUUID().equals(leavedTeam.getLeader())).getOrElseThrow(() -> new NullPointerException("the leader is not online!")).getGameProfile());
                        } catch (CommandSyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        x1.leave(leaver.getUUID());
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException(e);
                    }


                });
    }

    @SubscribeEvent
    public void onKick(ServerKickPlayerEvent event) {
        ServerPlayer toKick = event.toKick;
        getTeamForPlayer(toKick)
                .filter(x -> x instanceof PartyTeam)
                .ifPresent(x -> {
                    PartyTeam x1 = (PartyTeam) x;
                    try {
                        x1.kick(new CommandSourceStack(null, null, null, toKick.serverLevel(), 0, "", Component.literal(""), toKick.getServer(), event.kicker), List.of(toKick.getGameProfile()));
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    @SubscribeEvent
    public void onPromotion(ServerPromoteEvent event) {
        getTeamForPlayer(event.promoted)
                .filter(x -> x instanceof PartyTeam)
                .ifPresent(x -> {
                    try {
                        if (((PartyTeam) x).isOwner(event.promoted.getUUID())) return;
                        ((PartyTeam) x).promote(event.from, List.of(event.promoted.getGameProfile()));
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
