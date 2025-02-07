package com.clefal.teams.client.gui.menu.invite;

import com.clefal.nirvana_lib.relocated.io.vavr.Tuple;
import com.clefal.nirvana_lib.relocated.io.vavr.Tuple2;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.components.ATEntryList;
import com.clefal.teams.client.gui.util.PlayerWithSkin;
import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class AvailablePlayersList extends ATEntryList {
    public boolean updated = false;

    Function<Component, Integer> getWidth = component -> Minecraft.getInstance().font.width(component.getString());

    Minecraft client = Minecraft.getInstance();
    Font font = client.font;
    int textHeight = font.lineHeight;
    ResourceLocation fail = ResourceLocation.tryParse("fail:fail");

    public AvailablePlayersList(int width, int height, int y0, int y1, List<String> players) {
        super(width, height, y0, y1);
        for (int i = 0; i < players.size(); i++) {
            this.addEntry(new PlayerEntry(players.get(i), fail));
        }
    }

    public void updateList(List<PlayerWithSkin> players) {
        if (this.updated) AdvancedTeam.LOGGER.warn("already update the invite screen!");
        ArrayList<ResourceLocation> resourceLocations = new ArrayList<>();
        players.forEach(x -> {
            GameProfile dummy = new GameProfile(UUID.randomUUID(), "");
            dummy.getProperties().put("textures", new Property("textures", x.value(), x.signature()));
            Minecraft.getInstance().getSkinManager().registerSkins(dummy, (type, id, texture) -> {
                if (type == MinecraftProfileTexture.Type.SKIN) {
                    resourceLocations.add(id);
                } else {
                    resourceLocations.add(fail);
                }
            }, false);
        });

        this.clearEntries();
        for (int i = 0; i < players.size(); i++) {
            this.addEntry(new PlayerEntry(players.get(i).name(), resourceLocations.get(i)));
        }
        this.updated = true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int height = client.getWindow().getGuiScaledHeight() / 2;
        int width = client.getWindow().getGuiScaledWidth() / 2;
        if (!updated) {
            guiGraphics.drawString(font, Component.translatable("teams.menu.invite.need_update"), (width - getWidth.apply(Component.translatable("teams.menu.invite.need_update"))) / 2, height - (textHeight / 2), ChatFormatting.BLACK.getColor(), false);
        } else {
            if (this.getItemCount() == 0) {
                guiGraphics.drawString(font, Component.translatable("teams.menu.invite.no_available_players"), width - getWidth.apply(Component.translatable("teams.menu.invite.no_available_players")) / 2, height - (textHeight / 2), ChatFormatting.BLACK.getColor(), false);
            }
        }
    }

    public class PlayerEntry extends ATEntry {
        String name;
        ResourceLocation skin;
        Checkbox selected;

        public PlayerEntry(String name, ResourceLocation skin) {
            this.name = name;
            this.selected = new Checkbox(0, 0, 5, 5, Component.literal(""), false, false);
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return this.selected.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            this.selected.setX(left + ATEntryList.buttonXInterval);
            this.selected.setY(top + ATEntryList.buttonYInterval);
            this.selected.render(guiGraphics, mouseX, mouseY, partialTick);

            guiGraphics.drawString(Minecraft.getInstance().font, name, left + 24, top + 12 - (Minecraft.getInstance().font.lineHeight / 2), ChatFormatting.BLACK.getColor(), false);
        }
    }
}
