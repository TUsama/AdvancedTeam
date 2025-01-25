package com.clefal.teams.compat;

import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.IProperty;
import com.clefal.teams.client.core.property.IRenderable;
import com.clefal.teams.client.core.property.impl.Health;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.compat.property.MNSHealth;
import com.clefal.teams.compat.property.MNSHealthResource;
import com.clefal.teams.compat.property.MNSMagicShield;
import com.clefal.teams.compat.property.MNSOtherResource;
import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.clefal.teams.event.server.ServerGatherPropertyEvent;
import com.clefal.teams.server.propertyhandler.HandlerManager;
import com.clefal.teams.server.propertyhandler.IPropertyHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.robertx22.mine_and_slash.mmorpg.SlashRef;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourceType;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourcesData;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class MineAndSlashCompatModule implements ICompatModule, IPropertyHandler {
    public static final MineAndSlashCompatModule INSTANCE = new MineAndSlashCompatModule();
    private static MineAndSlashConfig config;

    public static MineAndSlashConfig getConfig() {
        if (config == null) {
            config = ConfigApiJava.registerAndLoadConfig(MineAndSlashConfig::new, RegisterType.CLIENT);
        }
        return config;
    }

    @Override
    public String getModID() {
        return SlashRef.MODID;
    }

    @Override
    public void whenEnable() {
        AdvancedTeam.LOGGER.info("Detected Mine And Slash, enable MineAndSlashCompatModule!");
        AdvancedTeam.eventBus.register(INSTANCE);
        HandlerManager.INSTANCE.registerHandler(INSTANCE);
        getConfig();
    }

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

    @Override
    @SubscribeEvent
    public void onRead(ClientReadPropertyEvent event) {
        if (!config.showModProperty) return;
        List<String> properties = event.properties;
        CompoundTag tag = event.tag;

        if (properties.contains(MNSHealth.KEY) && tag.contains(MNSHealth.KEY)) {
            MNSHealth mnsHealth = MNSHealth.fromString(tag.getString(MNSHealth.KEY));
            String ms = tag.getString(MNSMagicShield.KEY);
            MNSMagicShield shield;
            if (ms.isEmpty()) {
                shield = new MNSMagicShield(0, 0);
            } else {
                shield = MNSMagicShield.fromString(ms);
            }
            event.addResult(new MNSHealthResource(mnsHealth, shield));
        }
        for (ResourceType value : ResourceType.values()) {
            if (value == ResourceType.health || value == ResourceType.magic_shield) continue;
            String identifier = MNSOtherResource.identifier.apply(value);
            if (properties.contains(identifier) && tag.contains(identifier)) {
                MNSOtherResource mnsOtherResource = MNSOtherResource.fromNetworkString(tag.getString(identifier));
                event.addResult(mnsOtherResource);
            }
        }
    }

    @Override
    public void onRender(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate) {
        Option<IProperty> property = teammate.getProperty(MNSHealthResource.KEY);
        for (IProperty iProperty : property) {
            if (iProperty instanceof MNSHealthResource healthResource) {
                healthResource.update();
                healthResource.render(gui, teammate);
            }
        }

        int count = 0;
        PoseStack pose = gui.pose();
        pose.translate(0, IRenderable.oneEntryHeight, 0);
        pose.translate(IRenderable.iconAndTextInterval, 0, 0);
        for (ResourceType value : ResourceType.values()) {
            if (value == ResourceType.health || value == ResourceType.magic_shield) continue;

            String identifier = MNSOtherResource.identifier.apply(value);
            for (IProperty iProperty : teammate.getProperty(identifier)) {
                if (iProperty instanceof MNSOtherResource otherResource) {
                    if (otherResource.maxValue.equals(0.0f)) continue;
                    otherResource.render(gui, teammate);
                    count++;
                    if (count % 2 == 0) {
                        pose.translate(-MNSOtherResource.barWidth * 2, MNSOtherResource.barHeight, 0);
                    } else {
                        pose.translate(MNSOtherResource.barWidth, 0, 0);
                    }
                }
            }

        }
    }



    public static class MineAndSlashConfig extends Config {

        public boolean showModProperty = true;

        private MineAndSlashConfig() {
            super(AdvancedTeam.id("mine_and_slash_config"));
        }
    }
}
