package com.clefal.teams.mixin;

import com.mojang.authlib.GameProfile;
import com.clefal.teams.TeamsHUD;
import com.clefal.teams.core.IHasTeam;
import com.clefal.teams.core.ModTeam;
import com.clefal.teams.core.TeamData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements IHasTeam {
	@Shadow @Final public ServerPlayerGameMode gameMode;

	@Shadow public abstract ServerLevel serverLevel();

	@Unique
	private ModTeam team;

	public ServerPlayerMixin(Level $$0, BlockPos $$1, float $$2, GameProfile $$3) {
		super($$0, $$1, $$2, $$3);
	}


	@Override
	public boolean hasTeam() {
		return team != null;
	}

	@Override
	public ModTeam getTeam() {
		return team;
	}

	@Override
	public void setTeam(ModTeam team) {
		this.team = team;
	}

	@Override
	public boolean isTeammate(ServerPlayer other) {
		return team.equals(((IHasTeam) other).getTeam());
	}

	@Inject(at = @At(value = "TAIL"), method = "addAdditionalSaveData")
	private void writeCustomDataToNbt(CompoundTag nbt, CallbackInfo info) {
		if (team != null) {
			nbt.putString("playerTeam", team.getName());
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "readAdditionalSaveData")
	private void readCustomDataFromNbt(CompoundTag nbt, CallbackInfo info) {
		if (team == null && nbt.contains("playerTeam")) {
			team = TeamData.getOrMakeDefault(this.serverLevel().getServer()).getTeam(nbt.getString("playerTeam"));
			if (team == null || !team.hasPlayer(getUUID())) {
				team = null;
			}
		}
	}

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/level/ServerPlayer;getHealth()F",ordinal = 1), method = "doTick")
	private void playerTick(CallbackInfo info) {
		var player = (ServerPlayer) ((Object) this);
		TeamsHUD.onPlayerHealthUpdate(player,player.getHealth(),player.getFoodData().getFoodLevel());
	}

	@Override
	public boolean isSpectator() {
		return this.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
	}

	@Override
	public boolean isCreative() {
		return this.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
	}
}
