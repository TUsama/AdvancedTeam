package com.clefal.teams.server.propertyhandler;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.IRenderableProperty;
import com.clefal.teams.client.core.property.Health;
import com.clefal.teams.client.core.property.Hunger;
import com.clefal.teams.client.gui.hud.StatusOverlay;
import com.clefal.teams.client.gui.util.BufferInfo;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.config.ATConfig;
import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.clefal.teams.event.server.ServerGatherPropertyEvent;
import com.clefal.teams.server.ModComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.joml.Vector2f;

import java.util.List;

public class VanillaPropertyHandler implements IPropertyHandler {
    public static VanillaPropertyHandler INSTANCE = new VanillaPropertyHandler();


    @Override
    @SubscribeEvent
    public void onGather(ServerGatherPropertyEvent event) {
        System.out.println("gather!");
        ServerPlayer player = event.player;
        event.addProperty(new Health(player.getHealth(), player.getMaxHealth()), (property, compoundTag) -> compoundTag.putString(property.getIdentifier(), player.getHealth() + "/" + player.getMaxHealth()));
        event.addProperty(new Hunger(player.getFoodData().getFoodLevel()), (property, compoundTag) -> compoundTag.putInt(property.getIdentifier(), player.getFoodData().getFoodLevel()));
    }

    @Override
    @SubscribeEvent
    public void onRead(ClientReadPropertyEvent event) {
        List<String> properties = event.properties;
        CompoundTag tag = event.tag;
        if (properties.contains(Health.KEY) && tag.contains(Health.KEY)) {
            event.addResult(Health.fromString(tag.getString(Health.KEY)));
        }

        if (properties.contains(Hunger.KEY) && tag.contains(Hunger.KEY)) {
            event.addResult(new Hunger(tag.getInt(Hunger.KEY)));
        }
    }

    @Override
    public Vector2f onRender(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate, Vector2f pos) {
        //System.out.println("render!");
        float relativeHeight = StatusOverlay.getRelativeHeight(0.04f);
        float relativeWidth = StatusOverlay.getRelativeWidth(0.03f);

        if (ATConfig.config.overlays.showHunger) {
            for (IRenderableProperty hunger : teammate.getProperty(Hunger.KEY)) {
                pos.add(0, relativeHeight);
                gui.blit(hunger.getResourceLocation(), ((int) pos.x), ((int) pos.y), 0, 0, 9, 9, 9, 9);
                //container.map.put(hunger.getResourceLocation(), BufferInfo.of(pos.x, y, 0, 0, 9, 9, 9, 9, gui.pose().last().pose()));
                pos.add(relativeWidth, 0);
                gui.drawString(Minecraft.getInstance().font, ModComponents.literal(hunger.getRenderString()), (int) pos.x, (int) pos.y, ChatFormatting.WHITE.getColor());
                pos.sub(relativeWidth, 0);
            }
        }

        if (ATConfig.config.overlays.showHealth) {
            for (IRenderableProperty health1 : teammate.getProperty(Health.KEY)) {
                if (health1 instanceof Health health){
                    pos.add(0, relativeHeight);
                    gui.blit(health.getResourceLocation(), ((int) pos.x), ((int) pos.y), 0, 0, 9, 9, 9, 9);
                    //a weird bug will appear if I use batch upload here.
                    //container.map.put(health.getResourceLocation(), BufferInfo.of(pos.x, y-10, 0, 0, 9, 9, 9, 9, gui.pose().last().pose()));
                    pos.add(relativeWidth, 0);
                    gui.drawString(Minecraft.getInstance().font, ModComponents.literal(health.getRenderString()), (int) pos.x, (int) pos.y, ChatFormatting.WHITE.getColor());
                    pos.sub(relativeWidth, 0);
                }
            }
        }



        return pos;
    }
}
