package com.clefal.teams.config;


import com.clefal.teams.AdvancedTeam;
import kotlin.ranges.IntRange;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

public class ATClientConfig extends Config {

    public static ATClientConfig config = ConfigApiJava.registerAndLoadConfig(ATClientConfig::new, RegisterType.CLIENT);

    public Overlays overlays = new Overlays();
    public Info info = new Info();

    public ATClientConfig() {
        super(AdvancedTeam.id("client_config"));
    }

    public static void init() {
    }

    public static class Overlays extends ConfigSection {
        public boolean enableCompassOverlay = true;

        public boolean enableStatusOverlay = true;

        public ConfigGroup statusOption = new ConfigGroup("status");
        public boolean showHunger = false;
        public boolean showHealth = true;
        public ValidatedFloat barAnimationSpeed = new ValidatedFloat(0.3f, 1.0f, 0.1f, ValidatedNumber.WidgetType.SLIDER);
        public int originX = 43;
        public int originY = 127;
        public ValidatedFloat scale = new ValidatedFloat(0.5f, 1.0f, 0.0f);
        public int maxEntryAmount = 5;
        @ConfigGroup.Pop
        public int entryInterval = 20;


    }
    public Button button = new Button();
    public static class Button extends ConfigSection{
        public boolean enableInventoryButton = true;
        public int inventoryButtonXOffset = 0;
        public int inventoryButtonYOffset = 0;
    }


    public static class Info extends ConfigSection {
        public ValidatedInt normalToastShowSecond = new ValidatedInt(3, 30, 0, ValidatedNumber.WidgetType.SLIDER);
        public ValidatedInt inviteToastShowSecond = new ValidatedInt(60, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX);
        public ValidatedInt applicationToastShowSecond = new ValidatedInt(60, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX);
    }


}
