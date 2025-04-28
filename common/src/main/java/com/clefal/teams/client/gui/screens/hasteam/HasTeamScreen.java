package com.clefal.teams.client.gui.screens.hasteam;

import com.clefal.nirvana_lib.relocated.io.vavr.collection.List;
import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.PlaceUtils;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.ClientTeamData;
import com.clefal.teams.client.gui.components.ATCheckBox;
import com.clefal.teams.client.gui.screens.ITeamMenu;
import com.clefal.teams.client.gui.screens.TeamsScreen;
import com.clefal.teams.client.gui.screens.invite.TeamsInvitePlayerScreen;
import com.clefal.teams.client.gui.screens.noteam.NoTeamScreen;
import com.clefal.teams.client.gui.screens.request.TeamApplicationScreen;
import com.clefal.teams.client.gui.toast.ToastConfigSave;
import com.clefal.teams.network.server.C2STeamLeavePacket;
import com.clefal.teams.network.server.config.C2STeamConfigSavePacket;
import com.clefal.teams.platform.Services;
import com.clefal.teams.server.ModComponents;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


public class HasTeamScreen extends TeamsScreen {

    private static final ResourceLocation TEXTURE = AdvancedTeam.id("textures/gui/screen_background.png");

    @Getter
    private HasTeamMenu currentMenu;

    public HasTeamScreen(Screen parent) {
        super(parent, ModComponents.TEAMS_MENU_TITLE);

    }

    public void changeMenu(HasTeamMenu menu) {
        if (currentMenu != null) {
            this.currentMenu.releaseAll();
        }
        this.currentMenu = menu;
        this.currentMenu.registerAll();
    }

    @Override
    protected void init() {
        super.init();
        List<HasTeamMenu> menus = List.of(new TeammateMenu(this));


        if (ClientTeam.INSTANCE.hasPermissions() || AdvancedTeam.IN_DEV) {
            menus = menus.prepend(new TeamConfigMenu(this)).reverse();
        }
        menus.forEach(ITeamMenu::init);

        int YCursor = this.y + 3;
        for (HasTeamMenuTab hasTeamMenuTab : menus.map(HasTeamMenu::getTab)) {
            addRenderableWidget(hasTeamMenuTab);
            hasTeamMenuTab.setPosition(this.x - hasTeamMenuTab.getWidth() + 3, YCursor);
            YCursor += (int) (hasTeamMenuTab.getHeight() * 1.5f);
        }

        changeMenu(menus.head());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        this.currentMenu.render(graphics, mouseX, mouseY, delta);
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

    public void refresh() {
        if (!ClientTeam.INSTANCE.isInTeam()) {
            minecraft.setScreen(parent);
        } else {
            minecraft.setScreen(new HasTeamScreen(parent));
        }
    }

    public static class TeammateMenu extends HasTeamMenu {

        private static final ResourceLocation ICON = AdvancedTeam.id("textures/gui/teammate.png");
        private TeammateEntryList entryList;
        private Button leaveTeamButton;
        private Button inviteButton;
        private Button checkRequest;

        public TeammateMenu(HasTeamScreen screen) {
            super(screen);
        }

        @Override
        public void registerAll() {
            screen.addRenderableWidget(entryList);
            screen.addRenderableWidget(leaveTeamButton);
            if (ClientTeam.INSTANCE.canInvite()){
                screen.addRenderableWidget(inviteButton);
            }
            screen.addRenderableWidget(checkRequest);
        }

        @Override
        public void releaseAll() {
            screen.removeWidget(entryList);
            screen.removeWidget(leaveTeamButton);
            screen.removeWidget(inviteButton);
            screen.removeWidget(checkRequest);
        }

        @Override
        public HasTeamMenuTab getTab() {
            return new HasTeamMenuTab(Component.translatable("teams.menu.tab.teammates"), screen, ICON) {
                @Override
                public void onPress() {
                    TeammateMenu teammateMenu = new TeammateMenu(screen);
                    teammateMenu.init();
                    screen.changeMenu(teammateMenu);
                }

                @Override
                public void renderIcon(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                    PoseStack pose = guiGraphics.pose();
                    pose.pushPose();
                    guiGraphics.blit(ICON, this.getX() + 8, this.getY() + 2, 13, 13, 0, 0, 64, 64, 64, 64);
                    pose.popPose();
                }
            };
        }

        @Override
        public void init() {
            //the y1 is from the two buttons' y
            this.entryList = new TeammateEntryList(screen);

            // Add menu buttons
            Minecraft minecraft = Minecraft.getInstance();

            this.leaveTeamButton = Button.builder(ModComponents.LEAVE_TEXT, button -> {
                NetworkUtils.sendToServer(new C2STeamLeavePacket());
                minecraft.setScreen(new NoTeamScreen(screen));
            }).bounds(screen.width / 2 - 125, screen.y + HEIGHT - 30, 80, 20).build();

            this.inviteButton = Button.builder(ModComponents.INVITE_TEXT, button -> minecraft.setScreen(new TeamsInvitePlayerScreen(screen)))
                    .bounds(screen.width / 2 - 40, screen.y + HEIGHT - 30, 80, 20).build();

            this.checkRequest = Button.builder(Component.translatable("teams.menu.teammates.check_request"), button -> minecraft.setScreen(new TeamApplicationScreen(screen)))
                    .bounds(screen.width / 2 + 45, screen.y + HEIGHT - 30, 80, 20).build();

            if (ClientTeam.INSTANCE.canInvite()){
                PlaceUtils.placeThreeButton(screen, leaveTeamButton, inviteButton, checkRequest);
            } else {
                PlaceUtils.placeTwoButton(screen, leaveTeamButton, checkRequest);
            }

        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {

        }
    }

    public static class TeamConfigMenu extends HasTeamMenu {

        private static final ResourceLocation ICON = AdvancedTeam.id("textures/gui/cogs.png");
        private Button saveButton;
        private ATCheckBox isPublic;
        private ATCheckBox everyoneInvite;

        public TeamConfigMenu(HasTeamScreen screen) {
            super(screen);
        }

        @Override
        public void registerAll() {
            screen.addRenderableWidget(saveButton);
            screen.addRenderableWidget(isPublic);
            screen.addRenderableWidget(everyoneInvite);
        }

        @Override
        public void releaseAll() {
            screen.removeWidget(saveButton);
            screen.removeWidget(isPublic);
            screen.removeWidget(everyoneInvite);
        }

        @Override
        public HasTeamMenuTab getTab() {
            return new HasTeamMenuTab(Component.translatable("teams.menu.tab.team_config"), screen, ICON) {


                @Override
                public void onPress() {
                    TeamConfigMenu teamConfigMenu1 = new TeamConfigMenu(screen);
                    teamConfigMenu1.init();
                    screen.changeMenu(teamConfigMenu1);
                }

                @Override
                public void renderIcon(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                    PoseStack pose = guiGraphics.pose();
                    pose.pushPose();
                    pose.translate(5, 0, 0);
                    guiGraphics.blit(ICON, this.getX() + 3, this.getY() + 3, 13, 13, 0, 0, 50, 50, 50, 50);
                    pose.popPose();
                }
            };
        }


        @Override
        public void init() {

            this.isPublic = new ATCheckBox(screen.x + 20, screen.y + 10, 10, 10, Component.translatable("teams.menu.team_config.is_public"), ClientTeamData.INSTANCE.isPublicTeam(ClientTeam.INSTANCE.getName()));

            this.everyoneInvite = new ATCheckBox(screen.x + 20, screen.y + 30, 10, 10, Component.translatable("teams.menu.team_config.everyone_can_invite"), ClientTeam.INSTANCE.allowEveryoneInvite());

            this.saveButton = Button.builder(Component.translatable("teams.menu.team_config.save"), button -> {
                NetworkUtils.sendToServer(new C2STeamConfigSavePacket(isPublic.selected(), everyoneInvite.selected()));
                new TeammateMenu(screen).getTab().onPress();
                Minecraft.getInstance().getToasts().addToast(new ToastConfigSave(ClientTeam.INSTANCE.getName()));
            }).build();

            PlaceUtils.placeOneButton(screen, this.saveButton);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {

        }
    }
}
