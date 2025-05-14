package com.clefal.teams.server.team;

import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.clefal.teams.server.ICodecProvider;

public interface IATServerTeamAttachment<T> extends ICodecProvider<T> {
    String getIdentifier();
}
