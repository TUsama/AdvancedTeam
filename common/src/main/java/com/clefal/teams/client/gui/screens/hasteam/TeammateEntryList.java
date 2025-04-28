package com.clefal.teams.client.gui.screens.hasteam;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.components.ATEntryList;
import com.clefal.teams.client.gui.components.ATEntryListTemplate;
import com.clefal.teams.client.gui.screens.TeamsScreen;
import com.clefal.teams.network.server.C2SPromotePacket;
import com.clefal.teams.network.server.C2STeamKickPacket;
import com.clefal.teams.platform.Services;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TeammateEntryList extends ATEntryListTemplate {


    public TeammateEntryList(TeamsScreen screen) {
        super(screen);
        int index = 0;
        for (var teammate : ClientTeam.INSTANCE.getTeammates()) {
            boolean local = minecraft.player.getUUID().equals(teammate.id);
            var entry = new TeammateEntryList.TeammateEntry(teammate, local, index);
            this.addEntry(entry);
            index++;
        }
        setRenderBackground(false);
        updateSize(this.width, this.height, this.y0, screen.getY() + TeamsScreen.HEIGHT - 32);
        setLeftPos(-10);
    }


    protected class TeammateEntry extends ATEntryList.ATEntry {

        private static final ResourceLocation TEXTURE = AdvancedTeam.id("textures/gui/screen_background.png");
        private final ResourceLocation FLAG = AdvancedTeam.id("textures/gui/flags.png");
        private final Minecraft client;
        private final ClientTeam.Teammate teammate;
        @Getter
        private final ImageButton kickButton;
        private final ImageButton promoteButton;
        private final boolean isLocal;

        public TeammateEntry(ClientTeam.Teammate teammate, boolean local, int index) {
            super();
            this.client = Minecraft.getInstance();
            this.teammate = teammate;
            int rowRight = getRowRight();
            int rowTop = getRowTop(index);
            {
                ImageButton kickButton = new ImageButton(rowRight - 24, rowTop + 8, 8, 8, 16, 190, TEXTURE, button -> {
                    NetworkUtils.sendToServer(new C2STeamKickPacket(ClientTeam.INSTANCE.getName(), teammate.id));
                    ClientTeam.INSTANCE.removePlayer(teammate.id);
                });
                kickButton.setTooltip(Tooltip.create(Component.translatable("teams.button.kick")));
                this.kickButton = kickButton;
                this.kickButton.active = false;
            }
            {
                ImageButton promoteButton = new ImageButton(rowRight - 48, rowTop + 8, 8, 8, 0, 0, 64, FLAG, 64, 128, button -> NetworkUtils.sendToServer(new C2SPromotePacket(teammate.id))){
                    @Override
                    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                        int i = 0;
                        if (!this.isActive()) {
                            i += 32 * 2;
                        } else if (this.isHoveredOrFocused()) {
                            i += 32;
                        }

                        RenderSystem.enableDepthTest();
                        RenderSystem.enableBlend();
                        guiGraphics.blit(FLAG, getX(), getY(), 8, 8, 0, i, 32, 32, 32, 64);
                    }
                };
                promoteButton.setTooltip(Tooltip.create(Component.translatable("teams.button.promote")));
                this.promoteButton = promoteButton;
                this.promoteButton.active = false;
            }
            this.isLocal = local;
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return this.kickButton.mouseClicked(mouseX, mouseY, button) || this.promoteButton.mouseClicked(mouseX, mouseY, button);
        }



        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            // Head
            float scale = 0.5F;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, 1.0f);
            PlayerFaceRenderer.draw(guiGraphics, teammate.skin, (int) ((left + 4) / scale), (int) ((top + 4) / scale), 32);
            guiGraphics.pose().popPose();
            // Nameplate
            guiGraphics.drawString(client.font, teammate.name, left + 24, top + 12 - (client.font.lineHeight / 2), ChatFormatting.BLACK.getColor(), false);
            // Buttons
            if (ClientTeam.INSTANCE.hasPermissions() && hovering) {
                if (isLocal){
                    if (AdvancedTeam.IN_DEV){
                        wakeButtons(guiGraphics, top, left, width, mouseX, mouseY, partialTick);
                    }
                } else {
                    wakeButtons(guiGraphics, top, left, width, mouseX, mouseY, partialTick);
                }
            } else {
                this.kickButton.active = false;
                this.promoteButton.active = false;
            }
        }

        private void wakeButtons(GuiGraphics guiGraphics, int top, int left, int width, int mouseX, int mouseY, float partialTick) {
            this.kickButton.active = true;
            this.promoteButton.active = true;
            this.kickButton.setX(left + width - ATEntryList.buttonXInterval);
            this.kickButton.setY(top + ATEntryList.buttonYInterval);
            this.promoteButton.setX(left + width - ATEntryList.buttonXInterval * 2);
            this.promoteButton.setY(top + ATEntryList.buttonYInterval);
            kickButton.render(guiGraphics, mouseX, mouseY, partialTick);
            promoteButton.render(guiGraphics, mouseX, mouseY, partialTick);
        }

    }
}
