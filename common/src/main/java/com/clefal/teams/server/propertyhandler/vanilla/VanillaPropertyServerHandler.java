package com.clefal.teams.server.propertyhandler.vanilla;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.client.core.property.impl.Health;
import com.clefal.teams.client.core.property.impl.Hunger;
import com.clefal.teams.event.server.ServerGatherPropertyEvent;
import com.clefal.teams.server.propertyhandler.IPropertyServerHandler;
import net.minecraft.server.level.ServerPlayer;

public class VanillaPropertyServerHandler implements IPropertyServerHandler {

    public static VanillaPropertyServerHandler INSTANCE = new VanillaPropertyServerHandler();

    @Override
    @SubscribeEvent
    public void onGather(ServerGatherPropertyEvent event) {
        ServerPlayer player = event.player;
        event.addProperty(new Health(player.getHealth(), player.getMaxHealth()), (property, compoundTag) -> compoundTag.putString(property.getIdentifier(), player.getHealth() + "/" + player.getMaxHealth()));
        event.addProperty(new Hunger(player.getFoodData().getFoodLevel()), (property, compoundTag) -> compoundTag.putInt(property.getIdentifier(), player.getFoodData().getFoodLevel()));
    }
}
