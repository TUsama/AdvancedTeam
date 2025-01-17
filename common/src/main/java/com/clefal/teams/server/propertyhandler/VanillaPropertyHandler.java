package com.clefal.teams.server.propertyhandler;

import com.clefal.nirvana_lib.relocated.io.vavr.collection.List;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.client.core.property.Health;
import com.clefal.teams.client.core.property.Hunger;
import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.clefal.teams.event.server.ServerGatherPropertyEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class VanillaPropertyHandler implements IPropertyHandler{
    public static VanillaPropertyHandler INSTANCE = new VanillaPropertyHandler();
    private final String healthKey = new Health(0.0f).getIdentifier();
    private final String hungerKey = new Hunger(0).getIdentifier();

    @Override
    @SubscribeEvent
    public void onGather(ServerGatherPropertyEvent event) {
        ServerPlayer player = event.player;
        event.addProperty(new Health(player.getHealth()), (property, compoundTag) -> {
            compoundTag.putFloat(property.getIdentifier(), player.getHealth());
        });
        event.addProperty(new Hunger(player.getFoodData().getFoodLevel()), (property, compoundTag) -> {
            compoundTag.putInt(property.getIdentifier(), player.getFoodData().getFoodLevel());
        });
    }

    @Override
    @SubscribeEvent
    public void onRead(ClientReadPropertyEvent event) {
        List<String> properties = event.properties;
        CompoundTag tag = event.tag;
        if (properties.contains(healthKey) && tag.contains(healthKey)){
            event.addResult(new Health(tag.getFloat(healthKey)));
        }

        if (properties.contains(hungerKey) && tag.contains(hungerKey)){
            event.addResult(new Hunger(tag.getInt(hungerKey)));
        }
    }
}
