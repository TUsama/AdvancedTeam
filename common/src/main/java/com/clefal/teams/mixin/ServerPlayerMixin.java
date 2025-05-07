package com.clefal.teams.mixin;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.property.impl.Health;
import com.clefal.teams.event.server.ServerFreezePropertyUpdateEvent;
import com.clefal.teams.event.server.ServerPlayerTickJobEvent;
import com.clefal.teams.network.client.S2CInvitationPacket;
import com.clefal.teams.network.client.S2CTeamPlayerDataPacket;
import com.clefal.teams.platform.Services;
import com.clefal.teams.server.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements IHasTeam, IPropertySender {
    @Unique
    private final List<Invitation> invitations = new ArrayList<>();
    @Unique
    private final Set<String> advancedTeam$updateKey = new HashSet<>(10);
    @Shadow
    @Final
    public ServerPlayerGameMode gameMode;
    @Unique
    private ATServerTeam team;

    @Shadow
    public abstract ServerLevel serverLevel();

    @Override
    public boolean hasTeam() {
        return team != null;
    }

    @Override
    public ATServerTeam getTeam() {
        return team;
    }

    @Override
    public void setTeam(ATServerTeam team) {
        this.team = team;
    }

    @Override
    public boolean isTeammate(ServerPlayer other) {
        return team.equals(((IHasTeam) other).getTeam());
    }

    @Override
    public List<Invitation> getInvitations() {
        return invitations;
    }


    @Inject(at = @At(value = "TAIL"), method = "addAdditionalSaveData")
    private void writeCustomDataToNbt(CompoundTag nbt, CallbackInfo info) {
        if (team != null) {
            nbt.putString("playerTeam", team.getName());
        }
    }

    @Inject(at = @At(value = "TAIL"), method = "readAdditionalSaveData")
    private void readCustomDataFromNbt(CompoundTag nbt, CallbackInfo info) {
        ServerPlayer serverPlayer = (ServerPlayer) (Object) this;
        if (team == null && nbt.contains("playerTeam")) {
            team = ATServerTeamData.getOrMakeDefault(this.serverLevel().getServer()).getTeam(nbt.getString("playerTeam"));
            if (team != null && !team.hasPlayer(serverPlayer)) {
                team = null;
            }
        }
    }

    @Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/level/ServerPlayer;getHealth()F", ordinal = 1), method = "doTick")
    private void UpdateHealthInfoForEveryone(CallbackInfo info) {
        this.addUpdate(Health.KEY);
    }

    @Inject(at = @At(value = "TAIL"), method = "tick")
    private void tickJob(CallbackInfo ci) {
        AdvancedTeam.post(new ServerPlayerTickJobEvent(self()));
    }

    private ServerPlayer self() {
        return (ServerPlayer) ((Object) this);
    }

    @Override
    public void addUpdate(String key) {
        this.advancedTeam$updateKey.add(key);
    }

    @Override
    public void clear() {
        this.advancedTeam$updateKey.clear();
    }

    @Override
    public void handleUpdate() {
        if (!advancedTeam$updateKey.isEmpty()) {
            com.clefal.nirvana_lib.relocated.io.vavr.collection.HashSet<String> result = AdvancedTeam.post(new ServerFreezePropertyUpdateEvent(this.advancedTeam$updateKey)).getResult();
            if (team != null) {
                List<ServerPlayer> players = team.getOnlinePlayers().asJava();
                S2CTeamPlayerDataPacket s2CTeamPlayerDataPacket = new S2CTeamPlayerDataPacket(self(), S2CTeamPlayerDataPacket.Type.UPDATE, result);
                //this keys collection is only initialized when we need to send an UPDATE type packet.
                NetworkUtils.sendToClients(s2CTeamPlayerDataPacket, players);
            }
            clear();
        }
    }
}
