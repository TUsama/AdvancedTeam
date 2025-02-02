package com.clefal.teams.client.gui.menu;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.network.server.C2SPromotePacket;
import com.clefal.teams.network.server.C2STeamKickPacket;
import com.clefal.teams.platform.Services;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TeamEntryList extends AbstractSelectionList<TeamEntryList.TeammateEntry> {


    public TeamEntryList(Minecraft minecraft, int width, int height, int y0, int y1) {
        super(minecraft, width, height, y0, y1, 24);

        int yPos = y0 + 4;
        int xPos = (this.width - TeammateEntry.WIDTH) / 2 + x0;

        for (var teammate : ClientTeam.INSTANCE.getTeammates()) {
            boolean local = minecraft.player.getUUID().equals(teammate.id);
            var entry = new TeamEntryList.TeammateEntry(teammate, xPos, yPos, local);
            this.addEntry(entry);
            yPos += 24;
        }

        this.setRenderBackground(false);
        this.setRenderHeader(false, 0);
        this.setRenderTopAndBottom(false);
    }


    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }

    protected class TeammateEntry extends AbstractSelectionList.Entry<TeammateEntry> {

        static final int WIDTH = 244;
        static final int HEIGHT = 24;
        private static final ResourceLocation TEXTURE = AdvancedTeam.id("textures/gui/screen_background.png");
        private final ResourceLocation FLAG = AdvancedTeam.id("textures/gui/flag.png");
        private final Minecraft client;
        private final ClientTeam.Teammate teammate;
        private final int x;
        private final int y;
        @Getter
        private final ImageButton kickButton;
        private final ImageButton promoteButton;
        private final boolean isLocal;

        public TeammateEntry(ClientTeam.Teammate teammate, int x, int y, boolean local) {
            super();
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


        private void renderBackground(GuiGraphics graphics) {
            graphics.blit(TEXTURE, x, y, 0, 166, WIDTH, HEIGHT);
        }



        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            // Background
            renderBackground(guiGraphics);
            // Head
            float scale = 0.5F;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.blit(teammate.skin, (int) ((x + 4) / scale), (int) ((y + 4) / scale), 32, 32, 32, 32);
            guiGraphics.pose().popPose();
            // Nameplate
            guiGraphics.drawString(client.font, teammate.name, x + 24, y + 12 - (client.font.lineHeight / 2), ChatFormatting.BLACK.getColor(), false);
            // Buttons
            if (ClientTeam.INSTANCE.hasPermissions() && getHovered() != null && getHovered().equals(this)) {
                if (isLocal){
                    if (AdvancedTeam.IN_DEV){
                        this.kickButton.active = true;
                        this.promoteButton.active = true;
                        kickButton.render(guiGraphics, mouseX, mouseY, partialTick);
                        promoteButton.render(guiGraphics, mouseX, mouseY, partialTick);
                    }
                } else {
                    this.kickButton.active = true;
                    this.promoteButton.active = true;
                    kickButton.render(guiGraphics, mouseX, mouseY, partialTick);
                    promoteButton.render(guiGraphics, mouseX, mouseY, partialTick);
                }
            } else {
                this.kickButton.active = false;
                this.promoteButton.active = false;
            }
        }
    }
}
