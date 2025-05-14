package com.clefal.teams.server;

import com.mojang.serialization.Codec;

public interface ICodecProvider<T> {
    Codec<T> getCodec();
}
