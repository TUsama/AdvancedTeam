package com.clefal.teams.client.gui.screens.noteam;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeamData;
import com.clefal.teams.client.gui.components.ATEntryList;
import com.clefal.teams.client.gui.components.ATEntryListTemplate;
import com.clefal.teams.client.gui.screens.TeamsScreen;
import com.clefal.teams.client.gui.toast.ToastApplying;
import com.clefal.teams.network.server.C2STeamApplyingPacket;
import com.clefal.teams.platform.Services;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TeamEntryList extends ATEntryListTemplate {
    public TeamEntryList(TeamsScreen screen) {
        super(screen);
        for (String team : ClientTeamData.INSTANCE.getOnlineTeams()) {
            if (!ClientTeamData.INSTANCE.isPublicTeam(team)) continue;
            var entry = new TeamEntryList.TeamEntry(team);
            this.addEntry(entry);
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


        public TeamEntry(String team) {
            super();
            this.client = Minecraft.getInstance();
            this.team = team;

            var button = new ImageButton(0, 0, 8, 8, 24, 190, TEXTURE, button1 -> {
                NetworkUtils.sendToServer(new C2STeamApplyingPacket(team));
                client.getToasts().addToast(new ToastApplying(team));
                client.setScreen(null);
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
