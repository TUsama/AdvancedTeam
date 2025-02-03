package com.clefal.teams.client.gui.menu;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.components.ATPlayerSuggestions;
import com.clefal.teams.client.gui.components.ATSuggestionsList;
import com.clefal.teams.client.gui.toast.ToastInviteSent;
import com.clefal.teams.server.ModComponents;
import com.clefal.teams.network.server.C2STeamInvitePacket;
import com.clefal.teams.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

public class TeamsInviteScreen extends TeamsInputScreen {

    private ATPlayerSuggestions playerSuggestions;

    public TeamsInviteScreen(Screen parent) {
        super(parent, ModComponents.INVITE_TITLE_TEXT);

    }

    @Override
    protected void init() {
        super.init();
        this.playerSuggestions = new ATPlayerSuggestions(Minecraft.getInstance(), this, super.inputField, Minecraft.getInstance().font, true, 0, 5, true, FastColor.ARGB32.color(100, 0, 0, 0));
    }

    @Override
    protected Component getSubmitText() {
        return ModComponents.INVITE_TEXT2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        this.playerSuggestions.render(graphics, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.playerSuggestions.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.playerSuggestions.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.playerSuggestions.mouseScrolled(delta);
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    protected void onSubmit(Button widget) {
        Services.PLATFORM.sendToServer(new C2STeamInvitePacket(inputField.getValue()));
        minecraft.getToasts().addToast(new ToastInviteSent(ClientTeam.INSTANCE.getName(), inputField.getValue()));
        minecraft.setScreen(parent);
    }

    @Override
    protected boolean submitCondition() {
        String clientName = minecraft.player.getName().getString();
        return minecraft.getConnection().getOnlinePlayers()
                .stream()
                .anyMatch(entry -> !entry.getProfile().getName().equals(clientName) && entry.getProfile().getName().equals(inputField.getValue()));
    }
}
