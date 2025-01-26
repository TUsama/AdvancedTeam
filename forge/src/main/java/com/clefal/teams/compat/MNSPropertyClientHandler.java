package com.clefal.teams.compat;

import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.IProperty;
import com.clefal.teams.client.core.property.IRenderable;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.compat.property.MNSHealth;
import com.clefal.teams.compat.property.MNSHealthResource;
import com.clefal.teams.compat.property.MNSMagicShield;
import com.clefal.teams.compat.property.MNSOtherResource;
import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.clefal.teams.server.propertyhandler.IPropertyClientHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourceType;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class MNSPropertyClientHandler implements IPropertyClientHandler {
    public static final MNSPropertyClientHandler INSTANCE = new MNSPropertyClientHandler();

    private static MineAndSlashConfig config;

    public static MineAndSlashConfig getConfig() {
        if (config == null) {
            config = ConfigApiJava.registerAndLoadConfig(MineAndSlashConfig::new, RegisterType.CLIENT);
        }
        return config;
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
            super(AdvancedTeam.id("mine_and_slash_client_config"));
        }
    }
}
