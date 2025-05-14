package com.clefal.teams.server.team;

import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.clefal.teams.server.Application;
import com.clefal.teams.utils.ExtraCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class ATServerTeamMembership implements IPostProcess {
    public static Codec<ATServerTeamMembership> CODEC = RecordCodecBuilder.create(atServerTeamMembershipInstance -> atServerTeamMembershipInstance.group(
                    ExtraCodecs.transformToSet(UUIDUtil.CODEC.listOf()).fieldOf("members").forGetter(ATServerTeamMembership::getMembers),
                    UUIDUtil.CODEC.fieldOf("leader").forGetter(x -> x.leader),
                    ExtraCodecs.transformToSet(Application.CODEC.listOf()).fieldOf("applications").forGetter(x -> x.applications)
            ).apply(atServerTeamMembershipInstance, ATServerTeamMembership::new)
    );
    private Set<UUID> members = new LinkedHashSet<>();
    @Nonnull
    @Setter
    private UUID leader;
    private Map<UUID, ServerPlayer> onlineMembers = new LinkedHashMap<>();
    private Set<Application> applications = new HashSet<>();

    public ATServerTeamMembership(Set<UUID> members, @Nonnull UUID leader, Set<Application> applications) {
        this.members = members;
        this.leader = leader;
        this.applications = applications;
    }

    public com.clefal.nirvana_lib.relocated.io.vavr.collection.List<ServerPlayer> getOnlinePlayers() {
        return com.clefal.nirvana_lib.relocated.io.vavr.collection.List.ofAll(onlineMembers.values());
    }


    public void forAllOnlineMembers(Consumer<ServerPlayer> handle){
        for (ServerPlayer onlinePlayer : getOnlinePlayers()) {
            handle.accept(onlinePlayer);
        }
    }

    public boolean playerHasPermissions(ServerPlayer player) {
        return getLeader().equals(player.getUUID()) || player.hasPermissions(2);
    }

    @Override
    public void postProcess(ServerLevel level, ATServerTeamData data, ATServerTeam team) {
        for (UUID member : members) {
            ServerPlayer playerByUUID = level.getServer().getPlayerList().getPlayer(member);
            if (playerByUUID != null) {
                onlineMembers.put(member, playerByUUID);
            }
        }

    }
}
