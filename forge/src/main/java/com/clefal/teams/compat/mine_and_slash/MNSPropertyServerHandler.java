package com.clefal.teams.compat.mine_and_slash;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.client.core.property.impl.Health;
import com.clefal.teams.compat.mine_and_slash.property.MNSHealth;
import com.clefal.teams.compat.mine_and_slash.property.MNSMagicShield;
import com.clefal.teams.compat.mine_and_slash.property.MNSOtherResource;
import com.clefal.teams.event.server.ServerGatherPropertyEvent;
import com.clefal.teams.server.propertyhandler.IPropertyServerHandler;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourceType;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourcesData;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import net.minecraft.server.level.ServerPlayer;

public class MNSPropertyServerHandler implements IPropertyServerHandler {
    public static final MNSPropertyServerHandler INSTANCE = new MNSPropertyServerHandler();
    @Override
    @SubscribeEvent
    public void onGather(ServerGatherPropertyEvent event) {
        event.gather.removeIf(next -> next._1.getIdentifier().equals(Health.KEY));
        ServerPlayer player = event.player;
        //player.sendSystemMessage(Component.literal(HealthUtils.getCurrentHealth(player) + ""));
        ResourcesData resources = Load.Unit(player).getResources();
        MNSHealth mnsHealth = new MNSHealth(resources.get(player, ResourceType.health), resources.getMax(player, ResourceType.health));
        event.addProperty(mnsHealth, (property, compoundTag) -> compoundTag.putString(property.getIdentifier(), mnsHealth.getRenderString()));
        if (resources.getMax(player, ResourceType.magic_shield) > 0) {
            MNSMagicShield mnsMS = new MNSMagicShield(resources.get(player, ResourceType.magic_shield), resources.getMax(player, ResourceType.magic_shield));
            event.addProperty(mnsMS, (property, compoundTag) -> compoundTag.putString(property.getIdentifier(), mnsMS.getRenderString()));
        }


        for (ResourceType type : ResourceType.values()) {
            if (type == ResourceType.health || type == ResourceType.magic_shield) continue;
            MNSOtherResource mnsOtherResource = new MNSOtherResource(resources.get(player, type), resources.getMax(player, type), type);
            //System.out.println("put property " + mnsOtherResource.getIdentifier());
            event.addProperty(mnsOtherResource, (property, compoundTag) -> compoundTag.putString(property.getIdentifier(), mnsOtherResource.getNetworkString()));
        }

    }
}
