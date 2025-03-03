package com.clefal.teams.modules.internal.propertyhandler.vanilla;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.client.core.property.impl.Health;
import com.clefal.teams.client.core.property.impl.Hunger;
import com.clefal.teams.event.server.ServerGatherPropertyEvent;
import com.clefal.teams.modules.internal.propertyhandler.IPropertyServerHandler;
import net.minecraft.server.level.ServerPlayer;

public class VanillaPropertyServerHandler implements IPropertyServerHandler {

    public static VanillaPropertyServerHandler INSTANCE = new VanillaPropertyServerHandler();

    @Override
    @SubscribeEvent
    public void onGather(ServerGatherPropertyEvent event) {
        ServerPlayer player = event.player;
        if (event.needUpdate(Health.KEY)){
            event.addProperty(new Health(player.getHealth(), player.getMaxHealth()), (property, compoundTag) -> compoundTag.putString(property.getIdentifier(), player.getHealth() + "/" + player.getMaxHealth()));
        }

        if (event.needUpdate(Hunger.KEY)) {
            event.addProperty(new Hunger(player.getFoodData().getFoodLevel()), (property, compoundTag) -> compoundTag.putInt(property.getIdentifier(), player.getFoodData().getFoodLevel()));
        }

    }
}
