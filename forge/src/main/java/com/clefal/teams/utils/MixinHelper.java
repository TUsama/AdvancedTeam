package com.clefal.teams.utils;

import com.clefal.teams.compat.mine_and_slash.MineAndSlashCompatModule;
import com.clefal.teams.server.IHasTeam;
import com.robertx22.library_of_exile.main.Packets;
import com.robertx22.mine_and_slash.vanilla_mc.packets.interaction.ExileInteractionResultPacket;
import com.robertx22.mine_and_slash.vanilla_mc.packets.interaction.IParticleSpawnMaterial;
import lombok.experimental.UtilityClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

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

}
