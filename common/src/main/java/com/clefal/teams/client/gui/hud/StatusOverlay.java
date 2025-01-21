package com.clefal.teams.client.gui.hud;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.property.Health;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.config.ATConfig;
import com.clefal.teams.server.propertyhandler.HandlerManager;
import com.clefal.teams.server.propertyhandler.IPropertyHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;

import java.util.List;

public class StatusOverlay {
    public static final StatusOverlay INSTANCE = new StatusOverlay();
    public boolean enabled = true;
    private final Minecraft client = Minecraft.getInstance();
    private final List<IPropertyHandler> handlers = HandlerManager.INSTANCE.getHandlers();
    private final VertexContainer container = new VertexContainer();
    private static final float startX = Minecraft.getInstance().getWindow().getGuiScaledWidth() * 0.003f;

    public StatusOverlay() {
    }

    public void render(GuiGraphics graphics) {
        if (!ATConfig.config.overlays.enableStatusHUD || !enabled) return;
        //if (client.screen != null) return;
        List<ClientTeam.Teammate> teammates = ClientTeam.INSTANCE.getTeammates();
        int shown = 0;
        Vector2f pos = new Vector2f(0, 0);
        pos.add(client.getWindow().getGuiScaledWidth() * 0.003f, 0);
        pos.add(0, (float) client.getWindow().getGuiScaledHeight() / 4 - 5);
        for (int i = 0; i < teammates.size() && shown < 4; ++i) {
            /*if (client.player.getUUID().equals(teammates.get(i).id)) {
                continue;
            }*/

           renderStatus(graphics, teammates.get(i), pos);
            ++shown;
        }
        this.container.draw(graphics.bufferSource());
        this.container.refresh();
    }

    private Vector2f renderStatus(GuiGraphics graphics, ClientTeam.Teammate teammate, Vector2f pos) {
        // Dont render dead players
        //Clefal: why?
        //if (teammate.getHealth() <= 0) return;
        //from left to right
        graphics.pose().pushPose();
        // Draw skin

        graphics.pose().scale(0.5F, 0.5F, 0);
        {
            pos.add(4, 0).set(pos.x, ((float) client.getWindow().getGuiScaledHeight() / 2 - 34 + 2 * pos.y));
            graphics.blit(teammate.skin, ((int) pos.x), (int) (pos.y), 32, 32, 32, 32);

        }



        // Draw name
        {
            pos.add(getRelativeWidth(0.035f), 0.0f)
                    //due to the scale()
                    .mul(2.0f, 1.0f);
            //System.out.println(pos.x);
            graphics.drawString(client.font, Component.literal(teammate.name), ((int) pos.x), ((int) (pos.y)), ChatFormatting.WHITE.getColor());
        }

        {
            for (IPropertyHandler handler : this.handlers) {
                handler.onRender(graphics, container, teammate, pos);
            }
        }
        graphics.pose().popPose();
        //System.out.println(teammate.getProperty(Health.KEY));





        // Update offset
        pos.add(0, 46).set(startX, pos.y);
        return pos;
    }

    public static float getRelativeWidth(float factor){
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() * factor;
    }
    public static float getRelativeHeight(float factor){
        return Minecraft.getInstance().getWindow().getGuiScaledHeight() * factor;
    }

}
