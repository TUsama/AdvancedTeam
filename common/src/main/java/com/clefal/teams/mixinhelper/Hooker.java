package com.clefal.teams.mixinhelper;

import com.clefal.teams.modules.internal.effect.VanillaPotionEffectModule;
import com.clefal.teams.modules.internal.effect.handlers.VanillaPotionEffectProperty;
import com.clefal.teams.network.client.S2CTeamPlayerDataPacket;
import com.clefal.teams.platform.Services;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.clefal.teams.server.IHasTeam;
import com.clefal.teams.server.IPropertySender;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class Hooker {
/*
    public static void UpdatePropertyInfoForEveryone(ServerPlayer player){
        ATServerTeam team = ATServerTeamData.getOrMakeDefault(player.server).getTeam(player);
        if (team != null) {
            List<ServerPlayer> players = team.getOnlinePlayers().asJava();
            NetworkUtils.sendToClients(new S2CTeamPlayerDataPacket(player, S2CTeamPlayerDataPacket.Type.UPDATE), players);
        }
    }
*/
    public static void updateEffects(ListTag listtag, LivingEntity entity){
        if (VanillaPotionEffectModule.isEnable){
            if (entity instanceof ServerPlayer player) {
                IHasTeam player1 = (IHasTeam) player;
                if (player1.hasTeam() && !listtag.isEmpty()) {
                    ((IPropertySender) player).addUpdate(VanillaPotionEffectProperty.KEY);
                    System.out.println("update potion effect!");
                }
            }
        }

    }


    public static void redirect0(GuiGraphics instance, int minX, int minY, int maxX, int maxY, int color){

    }

    public static void redirect1(GuiGraphics instance, int minX, int minY, int maxX, int maxY, int color){

    }
    public static void redirect2(GuiGraphics instance, int minX, int minY, int maxX, int maxY, int color){

    }
}
