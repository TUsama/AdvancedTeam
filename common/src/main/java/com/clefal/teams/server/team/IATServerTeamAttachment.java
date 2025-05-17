package com.clefal.teams.server.team;

import com.clefal.teams.server.ICodecProvider;

public interface IATServerTeamAttachment<T> extends ICodecProvider<T> {
    String getIdentifier();
}
