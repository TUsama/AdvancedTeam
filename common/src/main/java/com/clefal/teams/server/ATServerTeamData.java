package com.clefal.teams.server;

import com.clefal.nirvana_lib.relocated.io.vavr.control.Either;
import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.event.server.ServerJoinTeamEvent;
import com.clefal.teams.network.client.S2CTeamDataUpdatePacket;
import com.clefal.teams.utils.Failure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ATServerTeamData extends SavedData {

    private static final String TEAMS_KEY = "teams";

    private Map<String, ATServerTeam> teams = new HashMap<>();
    ServerLevel serverLevel;
    Scoreboard scoreboard;

    private ATServerTeamData(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
        scoreboard = serverLevel.getScoreboard();
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        toNBT(compoundTag);
        return compoundTag;
    }

    public com.clefal.nirvana_lib.relocated.io.vavr.collection.List<ATServerTeam> getTeams() {
        return com.clefal.nirvana_lib.relocated.io.vavr.collection.List.ofAll(teams.values());
    }

    private void announceUpdate(S2CTeamDataUpdatePacket.Type type, Collection<ServerPlayer> players, String... name){
        NetworkUtils.sendToClients(new S2CTeamDataUpdatePacket(type, name), players);
        setDirty();
    }


    public ATServerTeam createTeam(@NotNull String name,@NotNull ServerPlayer creator) {
        ATServerTeam team;
        if (((IHasTeam) creator).hasTeam()) {
            creator.sendSystemMessage(ModComponents.translatable("teams.error.alreadyinteam", creator.getName().getString()));
            team = ((IHasTeam) creator).getTeam();
        } else {
            team = new ATServerTeam.Builder(name).complete(this, creator.getUUID());
            teams.put(team.getName(), team);
            team.addPlayer(creator);
            team.promote(creator);

            List<ServerPlayer> players = creator.getServer().getPlayerList().getPlayers();
            announceUpdate(S2CTeamDataUpdatePacket.Type.ONLINE, players, team.getName());
        }

        return team;
    }

    public ATServerTeam createPublicTeam(@NotNull String name,@NotNull ServerPlayer creator) {
        ATServerTeam team = createTeam(name, creator);
        team.setPublic(true);
        return team;
    }

    public void disbandTeam(ATServerTeam team) {
        teams.remove(team.getName());
        MinecraftServer server = serverLevel.getServer();
        scoreboard.removePlayerTeam(scoreboard.getPlayerTeam(team.getName()));
        team.clear();
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        announceUpdate(S2CTeamDataUpdatePacket.Type.DISBAND, players, team.getName());
    }

    public boolean isEmpty() {
        return teams.isEmpty();
    }

    public boolean hasTeam(String team) {
        return teams.containsKey(team);
    }

    public ATServerTeam getTeam(ServerPlayer player) {
        return ((IHasTeam) player).getTeam();
    }

    public ATServerTeam getTeam(String name) {
        return teams.get(name);
    }

    public Either<Failure, Boolean> invitePlayerToTeam(ServerPlayer player, ATServerTeam team) {
        IHasTeam player1 = (IHasTeam) player;
        if (player1.hasTeam()) {
            return Either.left(Failure.in_a_team);
        } else {
            if (!player1.getInvitations().containsKey(team.getName())) {
                ((IHasTeam) player).addInvitation(new Invitation(team.getName()));
                return Either.right(true);
            } else {
                return Either.left(Failure.already_invite);
            }
        }
    }

    public boolean addPlayerToTeam(ServerPlayer player, ATServerTeam team){
        if (((IHasTeam) player).hasTeam()) {
            return false;
        } else {
            team.addPlayer(player);
            AdvancedTeam.post(new ServerJoinTeamEvent(team, player));
            return true;
        }

    }

    public boolean removePlayerFromTeam(ServerPlayer player) {
        ATServerTeam playerTeam = ((IHasTeam) player).getTeam();
        if (playerTeam == null) return false;
        playerTeam.removePlayer(player);
        if (playerTeam.isEmpty()) {
            disbandTeam(playerTeam);
        }
        return true;
    }

    public void fromNBT(CompoundTag compound) {
        teams.clear();
        ListTag list = compound.getList(TEAMS_KEY, Tag.TAG_COMPOUND);
        List<String> add = new ArrayList<>();
        for (var tag : list) {
            ATServerTeam atServerTeam = ATServerTeam.fromNBT((CompoundTag) tag, this);
            teams.put(atServerTeam.getName(), atServerTeam);
            add.add(atServerTeam.getName());
        }
        if (!list.isEmpty()){
            announceUpdate(S2CTeamDataUpdatePacket.Type.ADD, serverLevel.getServer().getPlayerList().getPlayers(), add.toArray(String[]::new));
        }
    }

    public void toNBT(CompoundTag compound) {
        ListTag list = new ListTag();
        for (var team : teams.values()) {
            list.add(team.toNBT());
        }
        compound.put(TEAMS_KEY, list);
    }

    


    static ATServerTeamData getOrMake(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(compoundTag -> loadStatic(compoundTag,serverLevel), () -> new ATServerTeamData(serverLevel), TEAMS_KEY);
    }

    public static ATServerTeamData getOrMakeDefault(MinecraftServer server) {
        return getOrMake(server.overworld());
    }

    public static ATServerTeamData loadStatic(CompoundTag compoundTag, ServerLevel level) {
        ATServerTeamData id = new ATServerTeamData(level);
        id.fromNBT(compoundTag);
        return id;
    }

}
