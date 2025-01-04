package com.clefal.teams.command;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.clefal.teams.core.ModTeam;
import com.clefal.teams.core.TeamDB;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.resources.ResourceLocation;

import java.util.stream.Stream;

public class TeamSuggestions {

    private TeamSuggestions() {
    }

    static final SuggestionProvider<CommandSourceStack> TEAMS = SuggestionProviders.register(new ResourceLocation("teams"), (context, builder) -> {
        Stream<ModTeam> teams = TeamDB.getOrMakeDefault(((CommandSourceStack)context.getSource()).getServer()).getTeams();
        teams.forEach(team -> {
            builder.suggest(team.getName());
        });
        return builder.buildFuture();
    });

}
