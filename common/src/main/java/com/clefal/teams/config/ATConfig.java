package com.clefal.teams.config;


import com.clefal.teams.TeamsHUD;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;

public class ATConfig extends Config {

    public static ATConfig config = ConfigApiJava.registerAndLoadConfig(ATConfig::new, RegisterType.CLIENT);

    public Overlays overlays = new Overlays();
    public Info info = new Info();

    public ATConfig() {
        super(TeamsHUD.id("config"));
    }

    public static void init() {
    }

    public static class Overlays extends ConfigSection {
        public boolean enableCompassHUD = true;

        public boolean enableStatusHUD = true;
        public ConfigGroup statusGroup = new ConfigGroup("status");
        public boolean showHunger = true;
        @ConfigGroup.Pop
        public boolean showHealth = true;

    }

    public static class Info extends ConfigSection {
        public ValidatedInt toastShowSecond = new ValidatedInt(3, 0, 10, ValidatedNumber.WidgetType.SLIDER);
    }


}
