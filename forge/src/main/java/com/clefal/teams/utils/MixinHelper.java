package com.clefal.teams.utils;

import com.clefal.teams.compat.ftbteams.FTBTeamsCompatModule;
import com.clefal.teams.compat.mine_and_slash.MineAndSlashCompatModule;
import com.clefal.teams.compat.mine_and_slash.property.MNSStatusEffect;
import com.clefal.teams.server.IHasTeam;
import com.clefal.teams.server.IPropertySender;
import com.robertx22.library_of_exile.main.Packets;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import com.robertx22.mine_and_slash.vanilla_mc.packets.interaction.ExileInteractionResultPacket;
import com.robertx22.mine_and_slash.vanilla_mc.packets.interaction.IParticleSpawnMaterial;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
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
                    if (source.distanceToSqr(onlinePlayer) <= Math.pow(MineAndSlashCompatModule.getServerConfig().sendParticleWhenWithinRange.get(), 2)) Packets.sendToClient(onlinePlayer, new ExileInteractionResultPacket(target.getId(), notifier));
                }
            }
        }

    }

    public void updateStatusEffects(ServerPlayer player){
        if (MineAndSlashCompatModule.INSTANCE.isModuleEnabled){
            IPropertySender propertySender = (IPropertySender) player;
            IHasTeam hasTeam = (IHasTeam) player;
            if (hasTeam.hasTeam() && !Load.Unit(player).getStatusEffectsData().exileMap.isEmpty()){
                propertySender.addUpdate(MNSStatusEffect.KEY);
            }
        }
    }

    public void enableOfflineForFTBTeams(KnownClientPlayer selfKnown, Map<UUID, KnownClientPlayer> knownPlayers, CallbackInfo info){
        if (FTBTeamsCompatModule.INSTANCE.isModuleEnabled && FTBTeamsCompatModule.getServerConfig().enableOfflineSupport){
            String userName = Minecraft.getInstance().getUser().getName();
            UUID offlinePlayerUUID = UUIDUtil.createOfflinePlayerUUID(userName);
            selfKnown = knownPlayers.get(offlinePlayerUUID);
            if (selfKnown != null) {
                info.cancel();
            }
        }
    }
}
