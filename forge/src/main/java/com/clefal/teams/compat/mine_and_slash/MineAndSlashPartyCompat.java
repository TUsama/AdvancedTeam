package com.clefal.teams.compat.mine_and_slash;

import com.clefal.nirvana_lib.relocated.io.vavr.API;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.List;
import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.event.server.*;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.clefal.teams.server.IHasTeam;
import com.robertx22.mine_and_slash.capability.player.data.TeamData;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class MineAndSlashPartyCompat {

    public static final MineAndSlashPartyCompat INSTANCE = new MineAndSlashPartyCompat();

    @SubscribeEvent
    public void onPlayerOnline(ServerOnPlayerOnlineEvent event) {
        ServerPlayer player = event.player;
        if (((IHasTeam) player).hasTeam()) {
            ATServerTeam team1 = ((IHasTeam) player).getTeam();
            String name = team1.getName();
            TeamData team = Load.player(player).team;
            ((IMixinHelper) team).advancedTeam$setTeamID(name);
            if (team1.getLeader().equals(player.getUUID())) {
                team.promoteSelf();
            }
        } else {
            TeamData team = Load.player(player).team;
            if (!team.getTeam().isEmpty()) {
                //find others on server that have the same MNS team, and have a ATTeam at the same time.
                ATServerTeamData data = ATServerTeamData.getOrMakeDefault(player.getServer());
                Option.of(data.getTeam(Load.player(player).team.getTeam()))
                        .onEmpty(() -> {
                            List.ofAll(player.getServer().getPlayerList().getPlayers())
                                    .filter(x -> x.equals(player))
                                    .map(x -> API.Tuple(x, Load.player(x).team.getTeam()))
                                    .filter(x -> x._2().equals(team.getTeam()))
                                    .filter(x -> ((IHasTeam) x._1()).hasTeam())
                                    .headOption()
                                    .forEach(x -> ((IHasTeam) x._1()).getTeam().addPlayer(player));
                        })
                        .forEach(x -> x.addPlayer(player));
            }
        }
    }

    @SubscribeEvent
    public void onCreateTeam(ServerCreateTeamEvent event) {
        ServerPlayer player = event.player;
        TeamData team = Load.player(player).team;
        ATServerTeamData data = ATServerTeamData.getOrMakeDefault(player.getServer());
        if (!((IHasTeam) player).hasTeam()) {
            data.createTeam(team.getTeam(), (event.player));
        }
        ATServerTeam atServerTeam = data.getTeam(team.getTeam());

        ((IMixinHelper) team).advancedTeam$setTeamID(event.teamReadableName);
        team.promoteSelf();
        //compat for adding this mod for an already existed world
        player.getServer().getPlayerList().getPlayers().stream()
                .filter(x -> Load.player(x).team.getTeam().equals(team.getTeam()))
                .forEach(x -> {
                    if (!atServerTeam.hasPlayer(x)) {
                        atServerTeam.addPlayer(x);
                    }
                    if (Load.player(x).team.isLeader()) atServerTeam.promote(x);
                });
    }

    @SubscribeEvent
    public void onJoinTeam(ServerJoinTeamEvent event) {
        ServerPlayer joiner = event.joiner;

        TeamData team = Load.player(joiner).team;
        ((IMixinHelper) team).advancedTeam$setTeamID(event.team.name);
        ((IMixinHelper) team).advancedTeam$setIsLeader(false);


    }

    @SubscribeEvent
    public void onLeaveTeam(ServerPlayerLeaveEvent event) {
        ServerPlayer player = event.leaver;
        TeamData team = Load.player(player).team;
        team.leaveTeam();
    }

    @SubscribeEvent
    public void onKick(ServerKickPlayerEvent event) {
        ServerPlayer player = event.kicker;
        ServerPlayer toKick = player.getServer().getPlayerList().getPlayer(event.toKick);
        TeamData team = Load.player(toKick).team;
        team.leaveTeam();
    }
    
    @SubscribeEvent
    public void onPromotion(ServerPromoteEvent event){
        ServerPlayer promoted = event.promoted;
        TeamData team = Load.player(promoted).team;
        team.promoteSelf();
    }
}
