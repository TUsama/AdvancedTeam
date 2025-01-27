package com.clefal.teams.client.gui.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.server.ModComponents;
import com.clefal.teams.network.server.C2STeamKickPacket;
import com.clefal.teams.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class TeammateEntry extends AbstractWidget {

    static final int WIDTH = 244;
    static final int HEIGHT = 24;
    private static final ResourceLocation TEXTURE = AdvancedTeam.id("textures/gui/screen_background.png");

    private ImageButton kickButton;
    private final Minecraft client;
    private final ClientTeam.Teammate teammate;
    private final int x;
    private final int y;

    public TeammateEntry(ClientTeam.Teammate teammate, int x, int y, boolean local) {
        super(x,y,WIDTH,HEIGHT, ModComponents.literal(teammate.name));
        this.client = Minecraft.getInstance();
        this.teammate = teammate;
        this.x = x;
        this.y = y;
        if (ClientTeam.INSTANCE.hasPermissions()) {
            this.kickButton = new ImageButton(x + WIDTH - 24, y + 8, 8, 8, 16, 190, TEXTURE, button -> {
                Services.PLATFORM.sendToServer(new C2STeamKickPacket(ClientTeam.INSTANCE.getName(), teammate.id));
                ClientTeam.INSTANCE.removePlayer(teammate.id);
            });
        }
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        // Background
        renderBackground(graphics);
        // Head
        float scale = 0.5F;
        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, scale);
        graphics.blit(teammate.skin, (int) ((x + 4) / scale), (int) ((y + 4) / scale), 32, 32, 32, 32);
        graphics.pose().popPose();
        // Nameplate
        graphics.drawString(client.font, teammate.name, x + 24, y + 12 - (client.font.lineHeight / 2), ChatFormatting.BLACK.getColor(),false);
        // Buttons
        if (kickButton != null) {
            kickButton.render(graphics, mouseX, mouseY, delta);
        }
    }

    private void renderBackground(GuiGraphics graphics) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(TEXTURE, x, y, 0, 166, WIDTH, HEIGHT);
    }


    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.FOCUSED;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }


    public ImageButton getKickButton() {
        return kickButton;
    }
}
