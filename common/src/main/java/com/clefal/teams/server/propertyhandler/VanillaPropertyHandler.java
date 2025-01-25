package com.clefal.teams.server.propertyhandler;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.IProperty;
import com.clefal.teams.client.core.property.IRenderable;
import com.clefal.teams.client.core.property.impl.Health;
import com.clefal.teams.client.core.property.impl.Hunger;
import com.clefal.teams.client.gui.hud.StatusOverlay;
import com.clefal.teams.client.gui.util.VertexContainer;
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
import java.util.concurrent.ThreadLocalRandom;

public class VanillaPropertyHandler implements IPropertyHandler {
    public static VanillaPropertyHandler INSTANCE = new VanillaPropertyHandler();
    public static ThreadLocalRandom random = ThreadLocalRandom.current();

    @Override
    @SubscribeEvent
    public void onGather(ServerGatherPropertyEvent event) {
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
    public void onRender(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate) {


        for (IProperty hunger : teammate.getProperty(Hunger.KEY)) {
            if (hunger instanceof Hunger hunger1) hunger1.render(gui, teammate);
        }


        for (IProperty health1 : teammate.getProperty(Health.KEY)) {
            if (health1 instanceof Health health) health.render(gui, teammate);
        }

    }
}
