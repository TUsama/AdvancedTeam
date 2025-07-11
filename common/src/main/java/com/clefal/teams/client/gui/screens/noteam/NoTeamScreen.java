package com.clefal.teams.client.gui.screens.noteam;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.PlaceUtils;
import com.clefal.teams.client.core.ClientRenderPersistentData;
import com.clefal.teams.client.core.ClientTeamData;
import com.clefal.teams.client.gui.screens.TeamsCreateScreen;
import com.clefal.teams.client.gui.screens.TeamsScreen;
import com.clefal.teams.client.gui.screens.invite.CheckInvitationScreen;
import com.clefal.teams.server.ModComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


public class NoTeamScreen extends TeamsScreen {


    private static final ResourceLocation TEXTURE = AdvancedTeam.id("textures/gui/screen_background.png");
    private TeamEntryList entryList;

    public NoTeamScreen(Screen parent) {
        super(parent, ModComponents.LONELY_MENU_TITLE);
    }

    @Override
    protected void init() {
        super.init();
        // Menu buttons
        if (!ClientRenderPersistentData.getInstance().invitations.isEmpty() || AdvancedTeam.IN_DEV){
            for (Button button : PlaceUtils.placeTwoButton(this, Button.builder(ModComponents.CREATE_TEXT, button -> minecraft.setScreen(new TeamsCreateScreen(this)))
                    .bounds(this.width / 2 - 125, y + HEIGHT - 30, 80, 20).build(), Button.builder(Component.translatable("teams.menu.check_invitation"), button -> minecraft.setScreen(new CheckInvitationScreen(this))).bounds(this.width / 2 - 40, y + HEIGHT - 30, 80, 20).build())) {
                addRenderableWidget(button);
            }


        } else {
            addRenderableWidget(PlaceUtils.placeOneButton(this, Button.builder(ModComponents.CREATE_TEXT, button -> minecraft.setScreen(new TeamsCreateScreen(this)))
                    .bounds(this.width / 2 - 106, y + HEIGHT - 30, 100, 20).build()));

        }
        this.entryList = new TeamEntryList(this);
        addWidget(this.entryList);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        if (ClientTeamData.INSTANCE.noPublicTeam()) {
            int textWidth = font.width(ModComponents.LONELY_TEXT.getString());
            int textHeight = font.lineHeight;
            graphics.drawString(font, ModComponents.LONELY_TEXT, (this.width - textWidth) / 2, y + 24 - (textHeight / 2), ChatFormatting.BLACK.getColor(),false);
        } else {
            this.entryList.render(graphics, mouseX, mouseY, delta);
        }
    }

    public void refresh() {
        minecraft.setScreen(new NoTeamScreen(parent));
    }

    @Override
    protected int getWidth() {
        return WIDTH;
    }

    @Override
    protected int getHeight() {
        return HEIGHT;
    }

    @Override
    protected ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
