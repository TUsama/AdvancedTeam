package com.clefal.teams.compat.mine_and_slash;

import com.clefal.nirvana_lib.relocated.io.vavr.Function1;
import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.property.renderer.RendererManager;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.compat.mine_and_slash.property.*;
import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.clefal.teams.event.client.ClientRegisterPropertyRendererEvent;
import com.clefal.teams.modules.internal.propertyhandler.IProperty;
import com.clefal.teams.modules.internal.propertyhandler.IPropertyClientHandler;
import com.clefal.teams.modules.internal.propertyhandler.PositionContext;
import com.clefal.teams.utils.GuiUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.robertx22.mine_and_slash.database.registry.ExileDB;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourceType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.Iterator;
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

        if (properties.contains(MNSStatusEffect.KEY)) {
            if (tag.contains(MNSStatusEffect.KEY, 9)) {
                ListTag listTag = tag.getList(MNSStatusEffect.KEY, 10);
                ArrayList<MNSStatusEffectData> mnsStatusEffectData = new ArrayList<>();
                //System.out.println(listTag.size());
                for (int i = 0; i < listTag.size(); ++i) {
                    CompoundTag compoundTag = listTag.getCompound(i);
                    mnsStatusEffectData.add(new MNSStatusEffectData(compoundTag.getString("effect"), compoundTag.getInt("stack"), compoundTag.getInt("duration")));
                }
                event.addResult(new MNSStatusEffect(mnsStatusEffectData));
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
                        pose.translate(-getRelativeWidth(barWidth) / 2 * 2, getRelativeHeight(barHeight) / 3, 0);
                    } else {
                        pose.translate(getRelativeWidth(barWidth) / 2, 0, 0);
                    }
                }
            }

        }
        for (IProperty iProperty : teammate.getProperty(MNSStatusEffect.KEY)) {
            MNSStatusEffect effect = (MNSStatusEffect) iProperty;
            pose.pushPose();
            positionContext.setupEffectPosition(pose);

            int i = 0;

            for (Iterator<MNSStatusEffectData> var8 = effect.effects.iterator(); var8.hasNext(); i += PositionContext.iconSize + PositionContext.interval) {
                MNSStatusEffectData next = var8.next();
                //this can be buggy if I change the timing of sending effect packet.
                //currently the effect will always leave 1 tick.
                //do not use Continue, considering MNS adds so many effects.
                if (next.duration() == 1) var8.remove();

                var eff = ExileDB.ExileEffects().get(next.effect());

                gui.blit(eff.getTexture(), i, 0, PositionContext.iconSize, PositionContext.iconSize, 0, 0, 16, 16, 16, 16);

                GuiUtils.renderDuration(gui, next.duration(), pose, i, PositionContext.iconSize);

            }

            pose.popPose();
        }


    }

}
