package com.clefal.teams.utils;

import com.clefal.teams.compat.mine_and_slash.MineAndSlashCompatModule;
import com.clefal.teams.server.IHasTeam;
import com.robertx22.library_of_exile.main.Packets;
import com.robertx22.mine_and_slash.vanilla_mc.packets.interaction.ExileInteractionResultPacket;
import com.robertx22.mine_and_slash.vanilla_mc.packets.interaction.IParticleSpawnMaterial;
import jogamp.common.util.locks.SingletonInstanceServerSocket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class MixinHelper {

    public static void sendDMGParticleToTeammates(IParticleSpawnMaterial notifier, ServerPlayer source, LivingEntity target){
        if (MineAndSlashCompatModule.INSTANCE.isModuleEnabled && MineAndSlashCompatModule.getClientConfig().renderTeammateDamageParticle){
            IHasTeam player = (IHasTeam) source;
            if (player.hasTeam()) {
                for (ServerPlayer onlinePlayer : player.getTeam().getOnlinePlayers()) {
                    if (source.distanceTo(onlinePlayer) <= Math.pow(MineAndSlashCompatModule.getClientConfig().renderWhenWithinRange.get(), 2)) Packets.sendToClient(onlinePlayer, new ExileInteractionResultPacket(target.getId(), notifier));
                }
            }
        }

    }
}
