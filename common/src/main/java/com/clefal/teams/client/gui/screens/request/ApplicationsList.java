package com.clefal.teams.client.gui.screens.request;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientRenderPersistentData;
import com.clefal.teams.client.gui.components.ATEntryList;
import com.clefal.teams.client.gui.components.ATEntryListTemplate;
import com.clefal.teams.client.gui.components.ComponentButton;
import com.clefal.teams.client.gui.screens.TeamsScreen;
import com.clefal.teams.client.gui.util.PlayerWithSkin;
import com.clefal.teams.network.server.C2STeamJoinPacket;
import com.clefal.teams.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Function;

public class ApplicationsList extends ATEntryListTemplate {

    public boolean updated = false;
    Function<Component, Integer> getWidth = component -> Minecraft.getInstance().font.width(component.getString());
    ResourceLocation pending = AdvancedTeam.id("pending");
    Minecraft client = Minecraft.getInstance();
    Font font = client.font;
    int textHeight = font.lineHeight;

    public ApplicationsList(TeamsScreen screen) {
        super(screen);
        for (String app : ClientRenderPersistentData.getInstance().applications) {
            addEntry(new ApplicationEntry(app, pending));
        }
    }

    public void updateList(List<PlayerWithSkin> players) {
        if (this.updated) AdvancedTeam.LOGGER.warn("already update the application screen!");
        List<ResourceLocation> resourceLocations = players.stream().map(PlayerWithSkin::getSkin).toList();

        this.clearEntries();
        if (resourceLocations.isEmpty()) {
            this.updated = true;
            return;
        }
        for (int i = 0; i < players.size(); i++) {
            this.addEntry(new ApplicationEntry(players.get(i).name(), resourceLocations.get(i)));
        }

        this.updated = true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int height = client.getWindow().getGuiScaledHeight() / 2;
        int width = client.getWindow().getGuiScaledWidth() / 2;
        if (!updated) {
            guiGraphics.drawString(font, Component.translatable("teams.menu.invite.need_update"), (this.width - getWidth.apply(Component.translatable("teams.menu.invite.need_update"))) / 2, height - (textHeight / 2), ChatFormatting.BLACK.getColor(), false);
        } else {
            if (this.getItemCount() == 0) {
                guiGraphics.drawString(font, Component.translatable("teams.menu.apply.no_available_application"), (this.width - getWidth.apply(Component.translatable("teams.menu.apply.no_available_application"))) / 2, height - (textHeight / 2), ChatFormatting.BLACK.getColor(), false);
            }
        }
    }

    public class ApplicationEntry extends ATEntry {
        String name;
        ResourceLocation skin;
        Button accept;
        Button reject;

        public ApplicationEntry(String name, ResourceLocation skin) {
            this.name = name;
            this.skin = skin;
            this.accept = ComponentButton.builder(Component.translatable("teams.menu.invitation.accept"), button -> {
                        if (ClientRenderPersistentData.getInstance().applications.contains(name)) {
                            NetworkUtils.sendToServer(new C2STeamJoinPacket(name));
                            ClientRenderPersistentData.getInstance().applications.remove(name);
                        } else {
                            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("teams.menu.application_expired"));
                            removeEntry(this);
                        }

                    }, Component.literal("✔").withStyle(ChatFormatting.GREEN))
                    .size(8, 8)
                    .tooltip(Tooltip.create(Component.translatable("teams.menu.invitation.accept")))
                    .build();
            this.reject = ComponentButton.builder(Component.translatable("teams.menu.invitation.reject"), button -> ClientRenderPersistentData.getInstance().applications.remove(name), Component.literal("×").withStyle(ChatFormatting.RED))
                    .size(8, 8)
                    .tooltip(Tooltip.create(Component.translatable("teams.menu.invitation.reject")))
                    .build();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return this.accept.mouseClicked(mouseX, mouseY, button) || this.reject.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            guiGraphics.drawString(Minecraft.getInstance().font, name, left + 8, top + 12 - (Minecraft.getInstance().font.lineHeight / 2), ChatFormatting.BLACK.getColor(), false);
            if (hovering) {
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
