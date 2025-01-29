package com.clefal.teams.server.propertyhandler.vanilla;

import com.clefal.nirvana_lib.relocated.io.vavr.Function1;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.IProperty;
import com.clefal.teams.client.core.property.impl.Health;
import com.clefal.teams.client.core.property.impl.Hunger;
import com.clefal.teams.client.core.property.renderer.RendererManager;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.clefal.teams.event.client.ClientRegisterPropertyRendererEvent;
import com.clefal.teams.server.propertyhandler.HandlerManager;
import com.clefal.teams.server.propertyhandler.IPropertyClientHandler;
import com.clefal.teams.server.propertyhandler.PositionContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class VanillaPropertyClientHandler implements IPropertyClientHandler {

    public static VanillaPropertyClientHandler INSTANCE = new VanillaPropertyClientHandler();


    @Override
    @SubscribeEvent
    public void onReadProperty(ClientReadPropertyEvent event) {
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
    @SubscribeEvent
    public void onRegisterRenderer(ClientRegisterPropertyRendererEvent event) {
        event.register(Health.KEY, Function1.of(health -> Health.Renderer.getRenderer(((Health) health))));
        event.register(Hunger.KEY, Function1.of(hunger -> Hunger.Renderer.getRenderer(((Hunger) hunger))));
    }

    @Override
    public void onRender(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate, PositionContext positionContext) {


        for (IProperty hunger : teammate.getProperty(Hunger.KEY)) {
            if (hunger instanceof Hunger hunger1) RendererManager.getRenderer(hunger1).render(gui, container, teammate, positionContext);
        }


        for (IProperty health1 : teammate.getProperty(Health.KEY)) {
            if (health1 instanceof Health health) RendererManager.getRenderer(health).render(gui, container, teammate, positionContext);
        }

    }
}
