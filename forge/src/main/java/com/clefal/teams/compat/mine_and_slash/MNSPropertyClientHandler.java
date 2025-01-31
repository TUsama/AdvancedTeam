package com.clefal.teams.compat.mine_and_slash;

import com.clefal.nirvana_lib.relocated.io.vavr.Function1;
import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.IProperty;
import com.clefal.teams.client.core.property.Constants;
import com.clefal.teams.client.core.property.renderer.RendererManager;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.compat.mine_and_slash.property.MNSHealth;
import com.clefal.teams.compat.mine_and_slash.property.MNSHealthResource;
import com.clefal.teams.compat.mine_and_slash.property.MNSMagicShield;
import com.clefal.teams.compat.mine_and_slash.property.MNSOtherResource;
import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.clefal.teams.event.client.ClientRegisterPropertyRendererEvent;
import com.clefal.teams.server.propertyhandler.IPropertyClientHandler;
import com.clefal.teams.server.propertyhandler.PositionContext;
import com.mojang.blaze3d.vertex.PoseStack;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourceType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

import static com.clefal.teams.client.core.property.Constants.*;

public class MNSPropertyClientHandler implements IPropertyClientHandler {
    public static final MNSPropertyClientHandler INSTANCE = new MNSPropertyClientHandler();



    @Override
    @SubscribeEvent
    public void onReadProperty(ClientReadPropertyEvent event) {
        if (!MineAndSlashCompatModule.getClientConfig().showModProperty) return;
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
    @SubscribeEvent
    public void onRegisterRenderer(ClientRegisterPropertyRendererEvent event) {
        for (ResourceType value : ResourceType.values()) {
            if (value == ResourceType.health || value == ResourceType.magic_shield) continue;
            event.register(MNSOtherResource.identifier.apply(value), Function1.of(property -> MNSOtherResource.Renderer.getRenderer(((MNSOtherResource) property))));
        }

    }

    @Override
    public void onRender(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate, PositionContext positionContext) {
        Option<IProperty> property = teammate.getProperty(MNSHealthResource.KEY);
        for (IProperty iProperty : property) {
            if (iProperty instanceof MNSHealthResource healthResource) {
                healthResource.update();
                healthResource.render(gui, container, teammate, positionContext);
            }
        }
        //System.out.println("test!");
        int count = 0;
        PoseStack pose = gui.pose();

        pose.translate(0, positionContext.oneEntryHeight(), 0);

        pose.translate(positionContext.iconAndTextInterval(), 0, 0);

        for (ResourceType value : ResourceType.values()) {
            if (value == ResourceType.health || value == ResourceType.magic_shield) continue;

            String identifier = MNSOtherResource.identifier.apply(value);
            for (IProperty iProperty : teammate.getProperty(identifier)) {
                if (iProperty instanceof MNSOtherResource otherResource) {
                    if (otherResource.maxValue.equals(0.0f)) continue;
                    RendererManager.getRenderer(otherResource).render(gui, container, teammate, positionContext);
                    count++;
                    if (count % 2 == 0) {
                        pose.translate(-getRelativeWidth(Constants.barWidth) / 2 * 2, getRelativeHeight(barHeight) / 3, 0);
                    } else {
                        pose.translate(getRelativeWidth(Constants.barWidth) / 2, 0, 0);
                    }
                }
            }

        }
    }

}
