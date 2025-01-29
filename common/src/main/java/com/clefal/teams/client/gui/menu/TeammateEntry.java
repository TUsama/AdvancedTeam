package com.clefal.teams.client.gui.menu;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.network.server.C2SPromotePacket;
import com.clefal.teams.network.server.C2STeamKickPacket;
import com.clefal.teams.platform.Services;
import com.clefal.teams.server.ModComponents;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TeammateEntry extends AbstractWidget {

    static final int WIDTH = 244;
    static final int HEIGHT = 24;
    private static final ResourceLocation TEXTURE = AdvancedTeam.id("textures/gui/screen_background.png");
    private final ResourceLocation FLAG = AdvancedTeam.id("textures/gui/flag.png");
    private final Minecraft client;
    private final ClientTeam.Teammate teammate;
    private final int x;
    private final int y;
    private ImageButton kickButton;
    private ImageButton promoteButton;
    private boolean isLocal;

    public TeammateEntry(ClientTeam.Teammate teammate, int x, int y, boolean local) {
        super(x, y, WIDTH, HEIGHT, ModComponents.literal(teammate.name));
        this.client = Minecraft.getInstance();
        this.teammate = teammate;
        this.x = x;
        this.y = y;
        {
            ImageButton kickButton = new ImageButton(x + WIDTH - 24, y + 8, 8, 8, 16, 190, TEXTURE, button -> {
                Services.PLATFORM.sendToServer(new C2STeamKickPacket(ClientTeam.INSTANCE.getName(), teammate.id));
                ClientTeam.INSTANCE.removePlayer(teammate.id);
            });
            kickButton.setTooltip(Tooltip.create(Component.translatable("teams.button.kick")));
            this.kickButton = kickButton;
            this.kickButton.active = false;
        }
        {
            ImageButton promoteButton = new ImageButton(x + WIDTH - 48, y + 8, 8, 8, 0, 0, 8, FLAG, 8, 16, button -> Services.PLATFORM.sendToServer(new C2SPromotePacket(teammate.id)));
            promoteButton.setTooltip(Tooltip.create(Component.translatable("teams.button.promote")));
            this.promoteButton = promoteButton;
            this.promoteButton.active = false;
        }
        this.isLocal = local;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
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
        graphics.drawString(client.font, teammate.name, x + 24, y + 12 - (client.font.lineHeight / 2), ChatFormatting.BLACK.getColor(), false);
        // Buttons
        if (ClientTeam.INSTANCE.hasPermissions() && this.isHovered() && !isLocal) {
            this.kickButton.active = true;
            this.promoteButton.active = true;
            kickButton.render(graphics, mouseX, mouseY, delta);
            promoteButton.render(graphics, mouseX, mouseY, delta);
        } else {
            this.kickButton.active = false;
            this.promoteButton.active = false;
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
