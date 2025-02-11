package com.clefal.teams.client.gui.menu.invite;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.gui.components.ATEntryList;
import com.clefal.teams.client.gui.components.ATEntryListTemplate;
import com.clefal.teams.client.gui.menu.TeamsScreen;
import com.clefal.teams.client.gui.util.PlayerWithSkin;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class AvailablePlayersList extends ATEntryListTemplate {
    public boolean updated = false;

    Function<Component, Integer> getWidth = component -> Minecraft.getInstance().font.width(component.getString());

    Minecraft client = Minecraft.getInstance();
    Font font = client.font;
    int textHeight = font.lineHeight;
    ResourceLocation fail = ResourceLocation.tryParse("fail:fail");

    public AvailablePlayersList(TeamsScreen screen, List<String> players) {
        super(screen);
        for (int i = 0; i < players.size(); i++) {
            this.addEntry(new PlayerEntry(players.get(i), fail));
        }
    }

    public List<String> getAllSelection(){
        return this.children().stream().filter(x -> ((PlayerEntry)x).selected.selected()).map(x -> ((PlayerEntry) x).name).toList();
    }

    public void updateList(List<PlayerWithSkin> players) {
        if (this.updated) AdvancedTeam.LOGGER.warn("already update the invite screen!");
        ArrayList<ResourceLocation> resourceLocations = new ArrayList<>();
        players.forEach(x -> {
            if (!x.value().isEmpty()){
                GameProfile dummy = new GameProfile(UUID.randomUUID(), "");
                dummy.getProperties().put("textures", new Property("textures", x.value(), x.signature()));
                Minecraft.getInstance().getSkinManager().registerSkins(dummy, (type, id, texture) -> {
                    if (type == MinecraftProfileTexture.Type.SKIN) {
                        resourceLocations.add(id);
                    } else {
                        resourceLocations.add(DefaultPlayerSkin.getDefaultSkin(x.uuid()));
                    }
                }, false);
            } else {
                resourceLocations.add(DefaultPlayerSkin.getDefaultSkin(x.uuid()));
            }

        });
        this.clearEntries();
        if (resourceLocations.isEmpty()){
            this.updated = true;
            return;
        }
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
            guiGraphics.drawString(font, Component.translatable("teams.menu.invite.need_update"), (this.width - getWidth.apply(Component.translatable("teams.menu.invite.need_update"))) / 2, height - (textHeight / 2), ChatFormatting.BLACK.getColor(), false);
        } else {
            if (this.getItemCount() == 0) {
                guiGraphics.drawString(font, Component.translatable("teams.menu.invite.no_available_players"), (this.width - getWidth.apply(Component.translatable("teams.menu.invite.no_available_players"))) / 2, height - (textHeight / 2), ChatFormatting.BLACK.getColor(), false);
            }
        }
    }

    public class PlayerEntry extends ATEntry {
        String name;
        ResourceLocation skin;
        Checkbox selected;

        public PlayerEntry(String name, ResourceLocation skin) {
            this.name = name;
            this.skin = skin;
            this.selected = new Checkbox(0, 0, 20, 20, Component.literal(""), false, false);
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.selected.playDownSound(Minecraft.getInstance().getSoundManager());
            this.selected.onPress();
            return true;
        }



        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {

            float scale = 0.5f;
            this.selected.setX(((int) ((left + width - ATEntryList.buttonXInterval - 2) * (1 / scale))));
            this.selected.setY((int) ((top + ATEntryList.buttonYInterval - 2) * (1 / scale)));
            PoseStack pose = guiGraphics.pose();
            pose.pushPose();
            pose.scale(scale, scale, 1.0f);
            this.selected.render(guiGraphics, mouseX, mouseY, partialTick);
            PlayerFaceRenderer.draw(guiGraphics, this.skin, (int) ((left + 4) / scale), (int) ((top + 4) / scale), 32);
            pose.popPose();
            this.selected.setFocused(hovering);


            guiGraphics.drawString(Minecraft.getInstance().font, name, left + 24, top + 12 - (Minecraft.getInstance().font.lineHeight / 2), ChatFormatting.BLACK.getColor(), false);
        }
    }
}
