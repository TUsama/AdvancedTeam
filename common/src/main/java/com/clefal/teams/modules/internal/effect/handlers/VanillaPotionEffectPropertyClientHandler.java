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
                pose.translate(positionContext.iconAndTextInterval() + positionContext.barWidth() + 5, positionContext.barHeight(), 0);
                int interval = 2;
                int i = 0;
                int iconSize = 12;
                MobEffectTextureManager mobEffectTextureManager = Minecraft.getInstance().getMobEffectTextures();
                //pose.translate((float) iconSize / 2, 0, 0);
                for(Iterator var8 = property.getMobEffectInstance().iterator(); var8.hasNext(); i += iconSize + interval) {
                    MobEffectInstance mobEffectInstance = (MobEffectInstance)var8.next();
                    Component formatDuration;
                    int duration = mobEffectInstance.getDuration();
                    if (duration >= 20 * 60){
                        formatDuration =  Component.translatable("teams.effect_format.minute", duration / (20 * 60));
                    } else {
                        formatDuration = Component.translatable("teams.effect_format.second", duration / 20);
                    }

                    MobEffect mobEffect = mobEffectInstance.getEffect();
                    TextureAtlasSprite textureAtlasSprite = mobEffectTextureManager.get(mobEffect);

                    gui.blit(i, 0, 0, iconSize, iconSize, textureAtlasSprite);

                    String string = formatDuration.getString();
                    StringBuilder numberPart = new StringBuilder();
                    boolean hasWord = false;
                    for (char c : string.toCharArray()) {
                        if (Character.isDigit(c)) {
                            numberPart.append(c);
                        } else {
                            hasWord = true;
                            break;
                        }
                    }
                    String number = numberPart.toString();
                    pose.pushPose();
                    float scale = 0.6f;
                    pose.scale(scale,  scale, 1);
                    pose.translate(((i + (float) iconSize / 2)) / scale - 1, iconSize / scale - 2, 0);
                    char[] charArray = number.toCharArray();
                    //this can fix the problem of number overlap each other.
                    //is this because minecraft can't handle a too small interval?
                    for (int i1 = 0; i1 < charArray.length; i1++) {
                        char c = charArray[i1];
                        String s = String.valueOf(c);
                        gui.drawString(Minecraft.getInstance().font, s, -1, 0, ChatFormatting.WHITE.getColor());
                        if (!(i1 + 1 == charArray.length)){
                            //idk why this should add a 0.8f but it just works perfectly.
                            //can't use * scale here, the interval could be so weird.
                            pose.translate((Minecraft.getInstance().font.width(s) + 0.8f), 0, 0);
                        }
                    }


                    //gui.drawString(Minecraft.getInstance().font, number, -1, 0, ChatFormatting.WHITE.getColor());
                    if (hasWord){
                        pose.translate(Minecraft.getInstance().font.width(number) * scale, 0, 0);
                        gui.drawString(Minecraft.getInstance().font, string.replace(number, ""), -1, 0, ChatFormatting.WHITE.getColor());
                    }
                    pose.popPose();
                }

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
