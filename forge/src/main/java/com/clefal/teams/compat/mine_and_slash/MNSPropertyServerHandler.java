package com.clefal.teams.compat.mine_and_slash;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.client.core.property.impl.Health;
import com.clefal.teams.compat.mine_and_slash.property.*;
import com.clefal.teams.event.server.ServerFreezePropertyUpdateEvent;
import com.clefal.teams.event.server.ServerGatherPropertyEvent;
import com.clefal.teams.modules.internal.effect.handlers.VanillaPotionEffectProperty;
import com.clefal.teams.modules.internal.propertyhandler.IPropertyServerHandler;
import com.robertx22.library_of_exile.utils.GuiUtils;
import com.robertx22.mine_and_slash.database.data.exile_effects.ExileEffectInstanceData;
import com.robertx22.mine_and_slash.database.registry.ExileDB;
import com.robertx22.mine_and_slash.mmorpg.SlashRef;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourceType;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourcesData;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MNSPropertyServerHandler implements IPropertyServerHandler {
    public static final MNSPropertyServerHandler INSTANCE = new MNSPropertyServerHandler();

    @SubscribeEvent
    public void onFreeze(ServerFreezePropertyUpdateEvent event){
        //todo why this doesn't work?
        event.removeKey(Health.KEY);
    }

    @Override
    @SubscribeEvent
    public void onGather(ServerGatherPropertyEvent event) {
        ServerPlayer player = event.player;
        event.gather.removeIf(next -> next._1.getIdentifier().equals(Health.KEY));

        ResourcesData resources = Load.Unit(player).getResources();
        if (event.needUpdate(MNSHealth.KEY)){
            MNSHealth mnsHealth = new MNSHealth(resources.get(player, ResourceType.health), resources.getMax(player, ResourceType.health));
            event.addProperty(mnsHealth, (property, compoundTag) -> compoundTag.putString(property.getIdentifier(), mnsHealth.getRenderString()));
        }

        if (resources.getMax(player, ResourceType.magic_shield) > 0 && event.needUpdate(MNSMagicShield.KEY)) {
            MNSMagicShield mnsMS = new MNSMagicShield(resources.get(player, ResourceType.magic_shield), resources.getMax(player, ResourceType.magic_shield));
            event.addProperty(mnsMS, (property, compoundTag) -> compoundTag.putString(property.getIdentifier(), mnsMS.getRenderString()));
        }


        for (ResourceType type : ResourceType.values()) {
            if (type == ResourceType.health || type == ResourceType.magic_shield) continue;
            if (event.needUpdate(MNSOtherResource.identifier.apply(type))){
                MNSOtherResource mnsOtherResource = new MNSOtherResource(resources.get(player, type), resources.getMax(player, type), type);
                event.addProperty(mnsOtherResource, (property, compoundTag) -> compoundTag.putString(property.getIdentifier(), mnsOtherResource.getNetworkString()));
            }
        }

        if (event.needUpdate(MNSStatusEffect.KEY)){
            ListTag listTag = new ListTag();
            var exileMap = Load.Unit(player).getStatusEffectsData().exileMap;
            ArrayList<MNSStatusEffectData> mnsStatusEffectData = new ArrayList<>();

            for (Map.Entry<String, ExileEffectInstanceData> en : exileMap.entrySet()) {
                ExileEffectInstanceData value = en.getValue();
                if (!value.shouldRemove()) {
                    CompoundTag compoundTag = new CompoundTag();
                    compoundTag.putString("effect", en.getKey());
                    compoundTag.putInt("stack", value.stacks);
                    compoundTag.putInt("duration", value.ticks_left);
                    listTag.add(compoundTag);
                    mnsStatusEffectData.add(new MNSStatusEffectData(en.getKey(), value.stacks, value.ticks_left));
                }
            }
            event.addProperty(new MNSStatusEffect(mnsStatusEffectData), (iProperty, compoundTag) -> {
                compoundTag.put(MNSStatusEffect.KEY, listTag);
            });
        }

    }
}
