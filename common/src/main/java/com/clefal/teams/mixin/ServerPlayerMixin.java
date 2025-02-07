package com.clefal.teams.mixin;

import com.clefal.teams.mixinhelper.Hooker;
import com.clefal.teams.network.client.S2CInvitationPacket;
import com.clefal.teams.platform.Services;
import com.clefal.teams.server.Invitation;
import com.clefal.teams.server.IHasTeam;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements IHasTeam {
	@Shadow @Final public ServerPlayerGameMode gameMode;

	@Shadow public abstract ServerLevel serverLevel();

	@Unique
	private final List<Invitation> invitations = new ArrayList<>();

	@Unique
	private ATServerTeam team;



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

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/level/ServerPlayer;getHealth()F",ordinal = 1), method = "doTick")
	private void UpdateHealthInfoForEveryone(CallbackInfo info) {
		Hooker.UpdatePropertyInfoForEveryone((ServerPlayer) ((Object) this));
	}
	@Inject(at = @At(value  = "TAIL"), method = "tick")
	private void tickJob(CallbackInfo ci){
		ListIterator<Invitation> invitationListIterator = getInvitations().listIterator();
		while (invitationListIterator.hasNext()){
			Invitation next = invitationListIterator.next();
			if (next.update()) {
				invitationListIterator.remove();
				Services.PLATFORM.sendToClient(new S2CInvitationPacket(next.teamName, S2CInvitationPacket.Type.REMOVE), ((ServerPlayer)(Object) this));
			}
		}
	}

}
