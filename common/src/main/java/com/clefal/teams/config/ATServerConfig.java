package com.clefal.teams.config;

import com.clefal.teams.AdvancedTeam;
import me.fzzyhmstrs.fzzy_config.annotations.WithPerms;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

@WithPerms(opLevel = 2)
public class ATServerConfig extends Config {
    public static ATServerConfig config = ConfigApiJava.registerAndLoadConfig(ATServerConfig::new, RegisterType.BOTH);

    public ATServerConfig() {
        super(AdvancedTeam.id("server_config"));
    }

    public static void init() {
    }

    public enum Case{
        Disable,
        enable,
        force
    }

    public Case shareAchievements = Case.Disable;

    public Case enableVanillaTeamCompat = Case.Disable;

    public ValidatedInt invitationAndApplicationExpireTick = new ValidatedInt(20 * 60 * 2);
}
