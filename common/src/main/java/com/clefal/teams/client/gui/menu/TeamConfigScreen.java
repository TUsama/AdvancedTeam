package com.clefal.teams.client.gui.menu;

import com.clefal.teams.TeamsHUD;
import com.clefal.teams.server.ModComponents;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.scores.Team;

public class TeamConfigScreen extends TeamsScreen{
    static final int WIDTH = 256;
    static final int HEIGHT = 166;
    private static final ResourceLocation TEXTURE = TeamsHUD.id("textures/gui/screen_background.png");
    private final Checkbox seeFriendlyInvisiblesBox;
    private final Checkbox allowFriendlyFire;
    private final CycleButton<Team.Visibility> nameTagVisibility;
    private final CycleButton<Team.Visibility> deathMessageVisibility;
    private final CycleButton<Team.CollisionRule> collisionRule;

    public TeamConfigScreen(Screen parent, Component title) {
        super(parent, title);
        this.seeFriendlyInvisiblesBox = new Checkbox(x + 10, y + 10, 10, 10, ModComponents.CAN_SEE_FRIENDLY_INVISIBLES, true);
        this.allowFriendlyFire = new Checkbox(x + 20, y + 20, 10, 10, ModComponents.CAN_SEE_FRIENDLY_INVISIBLES, true);
        this.nameTagVisibility = CycleButton.<Team.Visibility>builder(visibility -> Component.translatable("teams.config.visibility." + visibility.toString().toLowerCase())).withInitialValue(Team.Visibility.HIDE_FOR_OTHER_TEAMS).withValues(Team.Visibility.values()).create(x + 30, y + 30, 10, 10, Component.literal(""));
        this.deathMessageVisibility = CycleButton.<Team.Visibility>builder(visibility -> Component.translatable("teams.config.visibility." + visibility.toString().toLowerCase())).withInitialValue(Team.Visibility.HIDE_FOR_OTHER_TEAMS).withValues(Team.Visibility.values()).create(x + 40, y + 40, 10, 10, Component.literal(""));
        this.collisionRule = CycleButton.<Team.CollisionRule>builder(rule -> Component.translatable("teams.config.collision_rule." + rule.toString().toLowerCase())).withInitialValue(Team.CollisionRule.ALWAYS).withValues(Team.CollisionRule.values()).create(x + 30, y + 30, 10, 10, Component.literal(""));
        //todo color config
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(this.seeFriendlyInvisiblesBox);
        addRenderableWidget(this.allowFriendlyFire);
        addRenderableWidget(this.nameTagVisibility);
        addRenderableWidget(this.deathMessageVisibility);
        addRenderableWidget(this.collisionRule);
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
