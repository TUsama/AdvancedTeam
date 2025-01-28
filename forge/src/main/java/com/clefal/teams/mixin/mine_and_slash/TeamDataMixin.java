package com.clefal.teams.mixin.mine_and_slash;

import com.clefal.teams.compat.mine_and_slash.IMixinHelper;
import com.robertx22.mine_and_slash.capability.player.data.TeamData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TeamData.class)
public class TeamDataMixin implements IMixinHelper {


    @Shadow(remap = false) private String team_id;

    @Shadow(remap = false) private boolean isLeader;

    @Override
    public void advancedTeam$setTeamID(String id) {
        this.team_id = id;
    }

    public void advancedTeam$setIsLeader(boolean is) {
        this.isLeader = is;
    }
}
