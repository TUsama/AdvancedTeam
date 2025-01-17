package com.clefal.teams.client.gui.menu;

import com.clefal.teams.client.core.ClientTeamData;
import com.clefal.teams.server.ModComponents;
import com.clefal.teams.network.server.C2STeamCreatePacket;
import com.clefal.teams.platform.Services;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TeamsCreateScreen extends TeamsInputScreen {

    public TeamsCreateScreen(Screen parent) {
        super(parent, ModComponents.CREATE_TITLE);
    }

    @Override
    protected Component getSubmitText() {
        return ModComponents.CREATE_TEXT2;
    }

    @Override
    protected void onSubmit(Button widget) {
        minecraft.setScreen(new TeamsMainScreen(null));
        Services.PLATFORM.sendToServer(new C2STeamCreatePacket(inputField.getValue()));
    }

    @Override
    protected boolean submitCondition() {
        return !ClientTeamData.INSTANCE.containsTeam(inputField.getValue());
    }


}
