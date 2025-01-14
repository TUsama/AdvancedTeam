package com.clefal.teams.network.client;

import com.clefal.teams.client.core.ClientTeam;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import com.clefal.teams.client.TeamsHUDClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class S2CTeamPlayerDataPacket implements S2CModPacket {

    public static final String ID_KEY = "playerUuid";
    public static final String NAME_KEY = "playerName";
    public static final String SKIN_KEY = "playerSkin";
    public static final String SKIN_SIG_KEY = "playerSkinSignature";
    public static final String HEALTH_KEY = "playerHealth";
    public static final String HUNGER_KEY = "playerHunger";
    public static final String TYPE_KEY = "actionType";

    public enum Type {
        ADD,
        UPDATE,
        REMOVE,
    }

    CompoundTag tag = new CompoundTag();

    public S2CTeamPlayerDataPacket(ServerPlayer player, Type type) {
        var health = player.getHealth();
        var hunger = player.getFoodData().getFoodLevel();
        tag.putUUID(ID_KEY, player.getUUID());
        tag.putString(TYPE_KEY, type.toString());
        switch (type) {
            case ADD -> {
                tag.putString(NAME_KEY, player.getName().getString());
                var properties = player.getGameProfile().getProperties();
                Property skin = null;
                if (properties.containsKey("textures")) {
                    skin = properties.get("textures").iterator().next();
                }
                tag.putString(SKIN_KEY, skin != null ? skin.getValue() : "");
                tag.putString(SKIN_SIG_KEY, skin != null ?
                        skin.getSignature() != null ? skin.getSignature() : ""
                        : "");
                tag.putFloat(HEALTH_KEY, health);
                tag.putInt(HUNGER_KEY, hunger);
            }
            case UPDATE -> {
                tag.putFloat(HEALTH_KEY, health);
                tag.putInt(HUNGER_KEY, hunger);
            }
        }
    }

    public S2CTeamPlayerDataPacket(FriendlyByteBuf byteBuf) {
        tag = byteBuf.readNbt();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeNbt(tag);
    }

    @Override
    public void handleClient() {
        UUID uuid = tag.getUUID(S2CTeamPlayerDataPacket.ID_KEY);
        switch (S2CTeamPlayerDataPacket.Type.valueOf(tag.getString(S2CTeamPlayerDataPacket.TYPE_KEY))) {
            case ADD -> {
                if (ClientTeam.INSTANCE.hasPlayer(uuid)) return;

                String name = tag.getString(S2CTeamPlayerDataPacket.NAME_KEY);
                float health = tag.getFloat(S2CTeamPlayerDataPacket.HEALTH_KEY);
                int hunger = tag.getInt(S2CTeamPlayerDataPacket.HUNGER_KEY);

                // Get skin data
                String skinVal = tag.getString(S2CTeamPlayerDataPacket.SKIN_KEY);
                String skinSig = tag.getString(S2CTeamPlayerDataPacket.SKIN_SIG_KEY);
                // Force download
                if (!skinVal.isEmpty()) {
                    GameProfile dummy = new GameProfile(UUID.randomUUID(), "");
                    dummy.getProperties().put("textures", new Property("textures", skinVal, skinSig));
                    Minecraft.getInstance().getSkinManager().registerSkins(dummy, (type, id, texture) -> {
                        if (type == MinecraftProfileTexture.Type.SKIN) {
                            ClientTeam.INSTANCE.addPlayer(uuid, name, id, health, hunger);
                        }
                    }, false);
                } else {
                    ClientTeam.INSTANCE.addPlayer(uuid, name, DefaultPlayerSkin.getDefaultSkin(uuid), health, hunger);
                }
            }
            case UPDATE -> {
                float health = tag.getFloat(S2CTeamPlayerDataPacket.HEALTH_KEY);
                int hunger = tag.getInt(S2CTeamPlayerDataPacket.HUNGER_KEY);
                ClientTeam.INSTANCE.updatePlayer(uuid, health, hunger);
            }
            case REMOVE -> {
                ClientTeam.INSTANCE.removePlayer(uuid);
            }
        }
    }
}
