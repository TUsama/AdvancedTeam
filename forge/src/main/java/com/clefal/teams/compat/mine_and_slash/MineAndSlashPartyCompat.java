package com.clefal.teams.compat.mine_and_slash;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.compat.ISubCompatModule;
import com.clefal.teams.event.server.ServerCreateTeamEvent;
import com.clefal.teams.event.server.ServerJoinTeamEvent;
import com.clefal.teams.event.server.ServerKickPlayerEvent;
import com.clefal.teams.event.server.ServerPlayerLeaveEvent;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.clefal.teams.server.IHasTeam;
import com.robertx22.mine_and_slash.capability.player.data.TeamData;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import net.minecraft.server.level.ServerPlayer;

public class MineAndSlashPartyCompat implements ISubCompatModule<MineAndSlashCompatModule> {
    public static final MineAndSlashPartyCompat INSTANCE = new MineAndSlashPartyCompat();
    @Override
    public MineAndSlashCompatModule getMainModule() {
        return MineAndSlashCompatModule.INSTANCE;
    }

    @Override
    public void whenEnable() {
        AdvancedTeam.registerAtServer(INSTANCE);
    }

    @SubscribeEvent
    public void onCreateTeam(ServerCreateTeamEvent event){
        ServerPlayer player = event.player;
        TeamData team = Load.player(player).team;
        ATServerTeamData data = ATServerTeamData.getOrMakeDefault(player.getServer());
        if (!((IHasTeam) player).hasTeam()){
            data.createTeam(team.team_id, (event.player));
        }
        ATServerTeam atServerTeam = data.getTeam(team.team_id);

        team.team_id = event.teamReadableName;
        team.isLeader = true;
        //compat for adding this mod for an already existed world
        player.getServer().getPlayerList().getPlayers().stream()
                .filter(x -> Load.player(x).team.team_id.equals(team.team_id))
                .forEach(x -> {
                    if (!atServerTeam.hasPlayer(x)){
                        atServerTeam.addPlayer(x);
                    }
                    if (Load.player(x).team.isLeader) atServerTeam.promote(x);
                });
    }

    @SubscribeEvent
    public void onJoinTeam(ServerJoinTeamEvent event){
        ServerPlayer joiner = event.joiner;
        TeamData team = Load.player(joiner).team;
        team.team_id = event.team.name;
        team.isLeader = false;
    }

    @SubscribeEvent
    public void onLeaveTeam(ServerPlayerLeaveEvent event){
        ServerPlayer player = event.leaver;
        TeamData team = Load.player(player).team;
        team.team_id = "";
        team.isLeader = false;
    }

    @SubscribeEvent
    public void onKick(ServerKickPlayerEvent event){
        ServerPlayer player = event.kicker;
        ServerPlayer toKick = player.getServer().getPlayerList().getPlayer(event.toKick);
        TeamData team = Load.player(toKick).team;
        team.team_id = "";
        team.isLeader = false;
    }

}
