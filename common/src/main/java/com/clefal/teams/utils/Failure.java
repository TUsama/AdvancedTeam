package com.clefal.teams.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public abstract class Failure {

    Component message;

    public abstract void announce(ServerPlayer player, Object... objects);



    public static Failure in_a_team = new Failure() {
        @Override
        public void announce(ServerPlayer player, Object... objects) {
            player.sendSystemMessage(Component.translatable("teams.error.alreadyinteam", objects[0]));
        }
    };

    public static Failure already_invite = new Failure() {
        @Override
        public void announce(ServerPlayer player, Object... objects) {
            player.sendSystemMessage(Component.translatable("teams.error.already_invite"));
        }
    };
}
