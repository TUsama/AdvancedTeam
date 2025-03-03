package com.clefal.teams.modules.internal.effect.handlers;

import com.clefal.nirvana_lib.relocated.io.vavr.Function1;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.property.impl.Hunger;
import com.clefal.teams.client.core.property.impl.PropertyRenderer;
import com.clefal.teams.client.core.property.renderer.RendererManager;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.clefal.teams.event.client.ClientRegisterPropertyRendererEvent;
import com.clefal.teams.modules.internal.propertyhandler.IProperty;
import com.clefal.teams.modules.internal.propertyhandler.IPropertyClientHandler;
import com.clefal.teams.modules.internal.propertyhandler.PositionContext;
import com.clefal.teams.utils.GuiUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VanillaPotionEffectPropertyClientHandler implements IPropertyClientHandler {
    public static final VanillaPotionEffectPropertyClientHandler INSTANCE = new VanillaPotionEffectPropertyClientHandler();
    @Override
    @SubscribeEvent
    public void onReadProperty(ClientReadPropertyEvent event) {
        List<String> properties = event.properties;
        CompoundTag tag = event.tag;
        if (properties.contains(VanillaPotionEffectProperty.KEY)) {
            if (tag.contains(VanillaPotionEffectProperty.KEY, 9)) {
                ListTag listTag = tag.getList(VanillaPotionEffectProperty.KEY, 10);
                ArrayList<MobEffectInstance> mobEffectInstances = new ArrayList<>();
                for (int i = 0; i < listTag.size(); ++i) {
                    CompoundTag compoundTag = listTag.getCompound(i);
                    MobEffectInstance mobEffectInstance = MobEffectInstance.load(compoundTag);
                    if (mobEffectInstance != null) {
                        mobEffectInstances.add(mobEffectInstance);
                    }
                }
                event.addResult(new VanillaPotionEffectProperty(mobEffectInstances));
            }
        }
    }

    @Override
    @SubscribeEvent
    public void onRegisterRenderer(ClientRegisterPropertyRendererEvent event) {
        event.register(VanillaPotionEffectProperty.KEY, Function1.of(potion -> new PropertyRenderer<>(((VanillaPotionEffectProperty) potion)) {
            @Override
            public void render(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate, PositionContext positionContext) {
                PoseStack pose = gui.pose();
                pose.pushPose();
                positionContext.setupEffectPosition(pose);

                int i = 0;
                int iconSize = PositionContext.iconSize;
                MobEffectTextureManager mobEffectTextureManager = Minecraft.getInstance().getMobEffectTextures();
                for(Iterator var8 = property.getMobEffectInstance().iterator(); var8.hasNext(); i += iconSize + PositionContext.interval) {
                    MobEffectInstance mobEffectInstance = (MobEffectInstance)var8.next();

                    MobEffect mobEffect = mobEffectInstance.getEffect();
                    TextureAtlasSprite textureAtlasSprite = mobEffectTextureManager.get(mobEffect);

                    gui.blit(i, 0, 0, iconSize, iconSize, textureAtlasSprite);

                    GuiUtils.renderDuration(gui, mobEffectInstance.getDuration(), pose, i, iconSize);

                }

                positionContext.finalEffectPosition().add(i, 0);
                pose.popPose();
            }
        }));
    }

    @Override
    public void onRender(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate, PositionContext positionContext) {
        for (IProperty iProperty : teammate.getProperty(VanillaPotionEffectProperty.KEY)) {
            RendererManager.getRenderer(iProperty).render(gui, container, teammate, positionContext);
        }

    }
}
