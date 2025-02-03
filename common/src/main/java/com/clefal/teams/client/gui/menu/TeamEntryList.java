package com.clefal.teams.client.gui.menu;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.ClientTeamData;
import com.clefal.teams.client.gui.toast.ToastRequest;
import com.clefal.teams.network.server.C2STeamRequestPacket;
import com.clefal.teams.platform.Services;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TeamEntryList extends AbstractSelectionList<TeamEntryList.TeamEntry> {
    public TeamEntryList(Minecraft minecraft, int width, int height, int y0, int y1) {
        super(minecraft, width, height, y0, y1, TeamEntry.HEIGHT);
        int yPos = y0 + 4;

        for (String team : ClientTeamData.INSTANCE.getOnlineTeams()) {
            var entry = new TeamEntryList.TeamEntry(team, this.width / 2 - 122, yPos);
            this.addEntry(entry);
            yPos += 24;
        }

        this.setRenderBackground(false);
        this.setRenderHeader(false, 24);
        this.setRenderTopAndBottom(false);
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    protected void renderHeader(GuiGraphics guiGraphics, int x, int y) {
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        guiGraphics.drawString(Minecraft.getInstance().font, "Avaliable Team", 0, 0, ChatFormatting.WHITE.getColor());
        pose.popPose();
    }

    public class TeamEntry extends AbstractSelectionList.Entry<TeamEntryList.TeamEntry> {

        static final int WIDTH = 244;
        static final int HEIGHT = 24;
        private static final ResourceLocation TEXTURE = AdvancedTeam.id("textures/gui/screen_background.png");

        public final ImageButton joinButton;
        private Minecraft client;
        private String team;
        private int x;
        private int y;

        public TeamEntry(String team, int x, int y) {
            super();
            this.client = Minecraft.getInstance();
            this.team = team;
            this.x = x;
            this.y = y;
            var button = new ImageButton(x + WIDTH - 24, y + 8, 8, 8, 24, 190, TEXTURE, button1 -> {
                Services.PLATFORM.sendToServer(new C2STeamRequestPacket(team));
                client.getToasts().addToast(new ToastRequest(team));
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

        private void renderBackground(GuiGraphics graphics) {
            graphics.blit(TEXTURE, x, y, 0, 166, WIDTH, HEIGHT);
        }


        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            // Background
            renderBackground(guiGraphics);
            // Name
            guiGraphics.drawString(client.font, team, x + 8, y + 12 - (int) (client.font.lineHeight / 2), ChatFormatting.BLACK.getColor());
            // Buttons
            if (ClientTeam.INSTANCE.hasPermissions() && getHovered() != null) {
                this.joinButton.active = true;
                joinButton.render(guiGraphics, mouseX, mouseY, partialTick);
            } else {
                this.joinButton.active = false;
            }

        }
    }
}
