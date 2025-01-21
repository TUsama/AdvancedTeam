package com.clefal.teams.platform.services;

import com.clefal.teams.network.client.S2CModPacket;
import com.clefal.teams.network.server.C2SModPacket;
import com.clefal.teams.platform.PhysicalSide;
import com.clefal.teams.platform.Platform;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.function.Function;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    Platform getPlatform();
    PhysicalSide getPhysicalSide();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }


    void sendToClient(S2CModPacket msg, ServerPlayer player);

    default void sendToClients(S2CModPacket msg, Collection<ServerPlayer> playerList) {
        playerList.forEach(player -> sendToClient(msg,player));
    }
    void sendToServer(C2SModPacket msg);


    <MSG extends S2CModPacket> void registerClientMessage(Class<MSG> packetClass, Function<FriendlyByteBuf,MSG> reader);

    <MSG extends C2SModPacket> void registerServerMessage(Class<MSG> packetClass, Function<FriendlyByteBuf,MSG> reader);

}