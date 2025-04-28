package com.clefal.teams.client.gui.screens;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.client.core.ClientTeamData;
import com.clefal.teams.client.gui.components.ATCheckBox;
import com.clefal.teams.client.gui.screens.hasteam.HasTeamScreen;
import com.clefal.teams.server.ModComponents;
import com.clefal.teams.network.server.C2STeamCreatePacket;
import com.clefal.teams.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TeamsCreateScreen extends TeamsInputScreen {
    ATCheckBox isPublic;

    public TeamsCreateScreen(Screen parent) {
        super(parent, ModComponents.CREATE_TITLE);
    }

    @Override
    protected Component getSubmitText() {
        return ModComponents.CREATE_TEXT2;
    }

    @Override
    protected void init() {
        super.init();
        this.isPublic = new ATCheckBox((int) (x + (float) (getWidth() - 100) / 2), y + 35, 10, 10, Component.translatable("teams.menu.team_config.is_public"), false, false);
        isPublic.setTooltip(Tooltip.create(Component.translatable("teams.menu.team_config.is_public.desc")));
        addWidget(this.isPublic);
    }


    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        graphics.drawString(Minecraft.getInstance().font, Component.translatable("teams.menu.create.public").getString(), (int) (x + (float) (getWidth() - 100) / 2) + 13, y + 36, ChatFormatting.WHITE.getColor());
        this.isPublic.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    protected void onSubmit(Button widget) {
        minecraft.setScreen(new HasTeamScreen(null));
        if (isPublic.selected()){
            NetworkUtils.sendToServer(C2STeamCreatePacket.createPublicTeam(inputField.getValue()));
        } else {
            NetworkUtils.sendToServer(C2STeamCreatePacket.createNonPublicTeam(inputField.getValue()));
        }

    }

    @Override
    protected boolean submitCondition() {
        return !ClientTeamData.INSTANCE.containsTeam(inputField.getValue()) && !inputField.getValue().isBlank() && inputField.getValue().length() < 100;
    }


}
