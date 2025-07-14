package com.clefal.teams.utils;

import com.clefal.teams.compat.mine_and_slash.MineAndSlashCompatModule;
import com.clefal.teams.modules.compat.ftbteams.FTBTeamsCompatModule;
import com.clefal.teams.server.IHasTeam;
import com.robertx22.library_of_exile.main.Packets;
import com.robertx22.mine_and_slash.vanilla_mc.packets.interaction.ExileInteractionResultPacket;
import com.robertx22.mine_and_slash.vanilla_mc.packets.interaction.IParticleSpawnMaterial;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@UtilityClass
public class MixinHelper {

    public void sendDMGParticleToTeammates(IParticleSpawnMaterial notifier, ServerPlayer source, LivingEntity target){
        if (MineAndSlashCompatModule.INSTANCE.isModuleEnabled && MineAndSlashCompatModule.getServerConfig().enableTeammateDamageParticle){
            IHasTeam player = (IHasTeam) source;
            if (player.hasTeam()) {
                for (ServerPlayer onlinePlayer : player.getTeam().getOnlinePlayers()) {
                    if (onlinePlayer.equals(source)) continue;
                    if (source.distanceToSqr(onlinePlayer) <= Math.pow(MineAndSlashCompatModule.getServerConfig().sendParticleWhenWithinRange.get(), 2)) Packets.sendToClient(onlinePlayer, new ExileInteractionResultPacket(target.getId(), notifier));
                }
            }
        }

    }


    public void enableOfflineForFTBTeams(KnownClientPlayer selfKnown, Map<UUID, KnownClientPlayer> knownPlayers, CallbackInfo info){
        if (FTBTeamsCompatModule.isModuleEnabled && FTBTeamsCompatModule.getServerConfig().enableOfflineSupport && !ModList.get().isLoaded("ftb_teams_offline_enabler")){
            String userName = Minecraft.getInstance().getUser().getName();
            UUID offlinePlayerUUID = UUIDUtil.createOfflinePlayerUUID(userName);
            selfKnown = knownPlayers.get(offlinePlayerUUID);
            if (selfKnown != null) {
                info.cancel();
            }
        }
    }
}
