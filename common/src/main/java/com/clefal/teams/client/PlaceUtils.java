package com.clefal.teams.client;

import com.clefal.teams.client.gui.screens.TeamsScreen;
import com.clefal.teams.mixin.AbstractWidgetMixin;
import com.clefal.teams.mixinhelper.IAbstractWidgetDuck;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.components.Button;

import java.util.List;

@UtilityClass
public class PlaceUtils {

    public List<Button> placeTwoButton(TeamsScreen screen, Button button1, Button button2){

        button1.setPosition(screen.width / 2 - 106, screen.getY() + TeamsScreen.HEIGHT - 30);
        ((IAbstractWidgetDuck) button1).advancedTeam$setWidthAndHeight(100, 20);

        button2.setPosition(screen.width / 2 + 6, screen.getY() + TeamsScreen.HEIGHT - 30);
        ((IAbstractWidgetDuck) button2).advancedTeam$setWidthAndHeight(100, 20);

        return List.of(button1, button2);
    }

    public List<Button> placeThreeButton(TeamsScreen screen, Button button1, Button button2, Button button3){
        button1.setPosition(screen.width / 2 - 125, screen.getY() + TeamsScreen.HEIGHT - 30);
        ((IAbstractWidgetDuck) button1).advancedTeam$setWidthAndHeight(80, 20);

        button2.setPosition(screen.width / 2 - 40 , screen.getY() + TeamsScreen.HEIGHT - 30);
        ((IAbstractWidgetDuck) button2).advancedTeam$setWidthAndHeight(80, 20);

        button3.setPosition(screen.width / 2 + 45 , screen.getY() + TeamsScreen.HEIGHT - 30);
        ((IAbstractWidgetDuck) button3).advancedTeam$setWidthAndHeight(80, 20);
        return List.of(button1, button2, button3);
    }

    public Button placeOneButton(TeamsScreen screen, Button button1){
        button1.setPosition(screen.width / 2 - 50, screen.getY() + TeamsScreen.HEIGHT - 30);
        ((IAbstractWidgetDuck) button1).advancedTeam$setWidthAndHeight(100, 20);
        return button1;
    }
}

