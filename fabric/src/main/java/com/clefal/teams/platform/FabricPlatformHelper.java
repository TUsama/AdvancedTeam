package com.clefal.teams.platform;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.network.ClientPacketHandlerFabric;
import com.clefal.teams.network.PacketHandlerFabric;
import com.clefal.teams.network.client.S2CModPacket;
import com.clefal.teams.network.server.C2SModPacket;
import com.clefal.teams.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Locale;
import java.util.function.Function;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public Platform getPlatform() {
        return Platform.FABRIC;
    }

    @Override
    public PhysicalSide getPhysicalSide() {
        switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> {
                return PhysicalSide.CLIENT;
            }
            case SERVER -> {
                return PhysicalSide.SERVER;
            }
        }
        return null;
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }


    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        msg.write(buf);
        ServerPlayNetworking.send(player,packet(msg.getClass()), buf);
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        msg.write(buf);
        ClientPlayNetworking.send(packet(msg.getClass()), buf);
    }



    @Override
    public <MSG extends S2CModPacket> void registerClientMessage(Class<MSG> packetClass, Function<FriendlyByteBuf, MSG> reader) {
        ClientPlayNetworking.registerGlobalReceiver(packet(packetClass), ClientPacketHandlerFabric.wrapS2C(reader));
    }

    @Override
    public <MSG extends C2SModPacket> void registerServerMessage(Class<MSG> packetClass, Function<FriendlyByteBuf, MSG> reader) {
        ServerPlayNetworking.registerGlobalReceiver(packet(packetClass), PacketHandlerFabric.wrapC2S(reader));
    }

    ResourceLocation packet(Class<?> clazz) {
        return AdvancedTeam.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
