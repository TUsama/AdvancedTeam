package com.clefal.teams.platform;

import com.clefal.teams.network.PacketHandlerForge;
import com.clefal.teams.network.client.S2CModPacket;
import com.clefal.teams.network.server.C2SModPacket;
import com.clefal.teams.platform.services.IPlatformHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Function;

public class ForgePlatformHelper implements IPlatformHelper {


    @Override
    public Platform getPlatform() {
        return Platform.FORGE;
    }

    @Override
    public PhysicalSide getPhysicalSide() {
        switch (FMLEnvironment.dist) {
            case CLIENT -> {
                return PhysicalSide.CLIENT;
            }
            case DEDICATED_SERVER -> {
                return PhysicalSide.SERVER;
            }
        }
        return null;
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }


    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {
        PacketHandlerForge.sendToClient(msg,player);
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        PacketHandlerForge.sendToServer(msg);
    }


    int i = 1024000;

    @Override
    public <MSG extends S2CModPacket> void registerClientMessage(Class<MSG> packetClass, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetClass, MSG::write, reader, PacketHandlerForge.wrapS2C());
    }

    int j = 5000;
    @Override
    public <MSG extends C2SModPacket> void registerServerMessage(Class<MSG> packetClass, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(j++, packetClass, MSG::write, reader, PacketHandlerForge.wrapC2S());
    }
}