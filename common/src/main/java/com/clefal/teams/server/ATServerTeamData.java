package com.clefal.teams.server;

import com.clefal.nirvana_lib.relocated.io.vavr.control.Either;
import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.event.server.ServerJoinTeamEvent;
import com.clefal.teams.network.client.S2CTeamDataUpdatePacket;
import com.clefal.teams.utils.Failure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
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
    ServerLevel serverLevel;
    Scoreboard scoreboard;
    private Map<String, ATServerTeam> teams = new HashMap<>();

    private ATServerTeamData(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
        scoreboard = serverLevel.getScoreboard();
    }

    static ATServerTeamData getOrMake(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(compoundTag -> loadStatic(compoundTag, serverLevel), () -> new ATServerTeamData(serverLevel), TEAMS_KEY);
    }

    public static ATServerTeamData getOrMakeDefault(MinecraftServer server) {
        return getOrMake(server.overworld());
    }

    public static ATServerTeamData loadStatic(CompoundTag compoundTag, ServerLevel level) {
        ATServerTeamData id = new ATServerTeamData(level);

        id.fromNBT(compoundTag);
        return id;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        //toNBT(compoundTag);
        ATServerTeam.CODEC.listOf().encodeStart(NbtOps.INSTANCE, new ArrayList<>(teams.values()))
                .resultOrPartial(error -> AdvancedTeam.LOGGER.error("Error on encoding team: {}", error))
                .ifPresent(x -> compoundTag.put(TEAMS_KEY, x));
        return compoundTag;
    }

    public com.clefal.nirvana_lib.relocated.io.vavr.collection.List<ATServerTeam> getTeams() {
        return com.clefal.nirvana_lib.relocated.io.vavr.collection.List.ofAll(teams.values());
    }

    private void announceUpdate(S2CTeamDataUpdatePacket.Type type, Collection<ServerPlayer> players, String... name) {
        NetworkUtils.sendToClients(new S2CTeamDataUpdatePacket(type, name), players);
        setDirty();
    }

    public ATServerTeam createTeam(@NotNull String name, @NotNull ServerPlayer creator) {
        ATServerTeam team;
        if (((IHasTeam) creator).hasTeam()) {
            creator.sendSystemMessage(ModComponents.translatable("teams.error.alreadyinteam", creator.getName().getString()));
            team = ((IHasTeam) creator).getTeam();
        } else {
            team = new ATServerTeam.Builder(name).complete(this, creator.getUUID());
            team.postProcess(serverLevel, this, team);
            teams.put(team.getCore().name(), team);
            team.addPlayer(creator);
            team.promote(creator);
            List<ServerPlayer> players = creator.getServer().getPlayerList().getPlayers();
            announceUpdate(S2CTeamDataUpdatePacket.Type.ONLINE, players, team.getCore().name());
        }

        return team;
    }

    public ATServerTeam createPublicTeam(@NotNull String name, @NotNull ServerPlayer creator) {
        ATServerTeam team = createTeam(name, creator);
        team.getConfig().isPublic = true;
        return team;
    }

    public void disbandTeam(ATServerTeam team) {
        team.onDisband();
        teams.remove(team.getCore().name());
        MinecraftServer server = serverLevel.getServer();
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        announceUpdate(S2CTeamDataUpdatePacket.Type.DISBAND, players, team.getCore().name());
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
            if (!player1.getInvitations().containsKey(team.getCore().name())) {
                ((IHasTeam) player).addInvitation(new Invitation(team.getCore().name()));
                return Either.right(true);
            } else {
                return Either.left(Failure.already_invite);
            }
        }
    }

    public boolean addPlayerToTeam(ServerPlayer player, ATServerTeam team) {
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
        ATServerTeam.CODEC.listOf().parse(NbtOps.INSTANCE, list)
                .resultOrPartial(x -> AdvancedTeam.LOGGER.error("Error on decoding team data: " + x))
                .ifPresent(x -> {
                    for (ATServerTeam atServerTeam : x) {
                        teams.put(atServerTeam.getCore().name(), atServerTeam);
                        add.add(atServerTeam.getCore().name());
                        atServerTeam.postProcess(serverLevel, this, atServerTeam);
                    }
                });
            /*ATServerTeam atServerTeam = ATServerTeam.fromNBT((CompoundTag) tag, this);
            teams.put(atServerteam.getCore().name(), atServerTeam);
            add.add(atServerteam.getCore().name());*/

        if (!list.isEmpty()) {
            announceUpdate(S2CTeamDataUpdatePacket.Type.ADD, serverLevel.getServer().getPlayerList().getPlayers(), add.toArray(String[]::new));
        }
    }

}
