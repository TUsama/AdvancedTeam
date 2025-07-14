package com.clefal.teams.client.gui.screens.invite;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.client.core.ClientRenderPersistentData;
import com.clefal.teams.client.gui.components.ATEntryList;
import com.clefal.teams.client.gui.components.ATEntryListTemplate;
import com.clefal.teams.client.gui.components.ComponentButton;
import com.clefal.teams.client.gui.screens.TeamsScreen;
import com.clefal.teams.network.server.C2STeamJoinPacket;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class InvitationEntryList extends ATEntryListTemplate {


    public InvitationEntryList(TeamsScreen screen) {
        super(screen);
        for (String invitation : ClientRenderPersistentData.getInstance().invitations) {
            this.addEntry(new InvitationEntry(invitation));
        }
    }


    public class InvitationEntry extends ATEntry{

        String Invitation;
        Button accept;
        Button reject;

        public InvitationEntry(String invitation) {
            Invitation = invitation;
            this.accept = ComponentButton.builder(Component.translatable("teams.menu.invitation.accept"), button -> {
                if (ClientRenderPersistentData.getInstance().invitations.contains(Invitation)){
                    NetworkUtils.sendToServer(new C2STeamJoinPacket(Invitation));
                    ClientRenderPersistentData.getInstance().invitations.remove(invitation);
                } else {
                    Minecraft.getInstance().player.sendSystemMessage(Component.translatable("teams.menu.invitation_expired"));
                    removeEntry(this);
                }

                    }, Component.literal("✔").withStyle(ChatFormatting.GREEN))
                    .size(8, 8)
                    .tooltip(Tooltip.create(Component.translatable("teams.menu.invitation.accept")))
                    .build();
            this.reject = ComponentButton.builder(Component.translatable("teams.menu.invitation.reject"), button -> ClientRenderPersistentData.getInstance().invitations.remove(Invitation), Component.literal("×").withStyle(ChatFormatting.RED))
                    .size(8, 8)
                    .tooltip(Tooltip.create(Component.translatable("teams.menu.invitation.reject")))
                    .build();

            this.accept.active = false;
            this.reject.active = false;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return this.accept.mouseClicked(mouseX, mouseY, button) || this.reject.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            guiGraphics.drawString(Minecraft.getInstance().font, Invitation, left + 8, top + 12 - (Minecraft.getInstance().font.lineHeight / 2), ChatFormatting.BLACK.getColor(), false);
            if (hovering){
                this.accept.active = true;
                this.reject.active = true;
                accept.setPosition(left + width - ATEntryList.buttonXInterval * 2, top + ATEntryList.buttonYInterval);
                reject.setPosition(left + width - ATEntryList.buttonXInterval, top + ATEntryList.buttonYInterval);
                accept.render(guiGraphics, mouseX, mouseY, partialTick);
                reject.render(guiGraphics, mouseX, mouseY, partialTick);
            } else {
                this.accept.active = false;
                this.reject.active = false;
            }
        }
    }
}
