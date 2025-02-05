package com.clefal.teams.client.gui.menu;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.ClientTeamData;
import com.clefal.teams.client.gui.components.ATEntryList;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TeamEntryList extends ATEntryList {
    public TeamEntryList(int width, int height, int y0, int y1) {
        super(width, height, y0, y1);

        int index = 0;
        for (String team : ClientTeamData.INSTANCE.getOnlineTeams()) {
            var entry = new TeamEntryList.TeamEntry(team, index);
            this.addEntry(entry);
            index++;
        }
        this.setRenderHeader(false, 10);

    }



    @Override
    protected void renderHeader(GuiGraphics guiGraphics, int x, int y) {
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("teams.menu.team_header"), 0, 0, ChatFormatting.BLACK.getColor());
        pose.popPose();
    }

    public class TeamEntry extends ATEntryList.ATEntry {

        private static final ResourceLocation TEXTURE = AdvancedTeam.id("textures/gui/screen_background.png");

        public final ImageButton joinButton;
        private Minecraft client;
        private String team;


        public TeamEntry(String team, int index) {
            super();
            this.client = Minecraft.getInstance();
            this.team = team;

            var button = new ImageButton(getRowRight() - 24, getRowTop(index) + 8, 8, 8, 24, 190, TEXTURE, button1 -> {
                return;
                /*Services.PLATFORM.sendToServer(new C2STeamRequestPacket(team));
                client.getToasts().addToast(new ToastRequest(team));
                client.setScreen(null);*/
            });
            button.setTooltip(Tooltip.create(Component.literal("request(not available)")));
            this.joinButton = button;
            this.joinButton.active = false;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return this.joinButton.mouseClicked(mouseX, mouseY, button);
        }


        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            // Name
            guiGraphics.drawString(client.font, team, left + 8, top + 12 - (int) (client.font.lineHeight / 2), ChatFormatting.BLACK.getColor(), false);
            // Buttons
            if (hovering) {
                this.joinButton.active = true;
                this.joinButton.setX(left + width - ATEntryList.buttonXInterval);
                this.joinButton.setY(top + ATEntryList.buttonYInterval);
                joinButton.render(guiGraphics, mouseX, mouseY, partialTick);
            } else {
                this.joinButton.active = false;
            }

        }
    }
}
