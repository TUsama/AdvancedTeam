package com.clefal.teams.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import lombok.experimental.UtilityClass;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class ExtraCodecs {


    public <T> Codec<Set<T>> transformToSet(Codec<List<T>> original){
        return original.xmap(HashSet::new, ArrayList::new);
    }
    public Codec<ChatFormatting> CHATFORMATTING = Codec.INT.comapFlatMap(s -> {
        ChatFormatting byId = ChatFormatting.getById(s);
        return byId != null ? DataResult.success(byId) : DataResult.error(() -> "can't find a ChatFormatting which index is " + s);
    }, Enum::ordinal);
}
