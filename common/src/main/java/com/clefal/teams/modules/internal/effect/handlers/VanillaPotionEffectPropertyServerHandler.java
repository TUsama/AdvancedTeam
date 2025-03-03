package com.clefal.teams.modules.internal.effect.handlers;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.event.server.ServerGatherPropertyEvent;
import com.clefal.teams.modules.internal.propertyhandler.IPropertyServerHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class VanillaPotionEffectPropertyServerHandler implements IPropertyServerHandler {
    public static final VanillaPotionEffectPropertyServerHandler INSTANCE = new VanillaPotionEffectPropertyServerHandler();
    @SubscribeEvent
    @Override
    public void onGather(ServerGatherPropertyEvent event) {
        ServerPlayer player = event.player;
        List<MobEffectInstance> activeEffects = new ArrayList<>(player.getActiveEffects());
        ListTag listTag = new ListTag();
        Iterator var3 = activeEffects.iterator();

        while(var3.hasNext()) {
            MobEffectInstance mobEffectInstance = (MobEffectInstance)var3.next();
            listTag.add(mobEffectInstance.save(new CompoundTag()));
        }

        event.addProperty(new VanillaPotionEffectProperty(activeEffects), (iProperty, compoundTag) -> {
            compoundTag.put(VanillaPotionEffectProperty.KEY, listTag);
        });

    }
}
