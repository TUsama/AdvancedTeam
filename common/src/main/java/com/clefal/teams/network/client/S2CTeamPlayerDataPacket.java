package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.relocated.io.vavr.API;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.HashSet;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.Event;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.event.client.ClientEvent;
import com.clefal.teams.event.server.ServerEvent;
import com.clefal.teams.event.server.ServerGatherPropertyEvent;
import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class S2CTeamPlayerDataPacket implements S2CModPacket {

    public static final String ID_KEY = "playerUuid";
    public static final String NAME_KEY = "playerName";
    public static final String SKIN_KEY = "playerSkin";
    public static final String SKIN_SIG_KEY = "playerSkinSignature";
    public static final String TYPE_KEY = "actionType";
    public List<String> propertiesName = new ArrayList<>();
    public HashSet<String> keys;

    public enum Type {
        ADD,
        UPDATE,
        REMOVE,
    }

    CompoundTag tag = new CompoundTag();
// health and hunger should be merged into the event system.
    public S2CTeamPlayerDataPacket(ServerPlayer player, Type type, HashSet<String> keys) {
        tag.putUUID(ID_KEY, player.getUUID());
        tag.putString(TYPE_KEY, type.toString());
        Runnable gather = () -> this.postAndDo(new ServerGatherPropertyEvent.ServerGatherAllPropertyEvent(player), event -> API.For(event.gather).yield().forEach(tuple2 -> {
            tuple2._2().accept(tuple2._1(), tag);
            this.propertiesName.add(tuple2._1().getIdentifier());
        }));
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
                //System.out.println("try run!");
                this.postAndDo(new ServerGatherPropertyEvent.ServerGatherAllPropertyEvent(player), event -> API.For(event.gather).yield().forEach(tuple2 -> {
                    tuple2._2().accept(tuple2._1(), tag);
                    this.propertiesName.add(tuple2._1().getIdentifier());
                }));
            }
            case UPDATE -> this.postAndDo(new ServerGatherPropertyEvent(player, keys), event -> API.For(event.gather).yield().forEach(tuple2 -> {
                tuple2._2().accept(tuple2._1(), tag);
                this.propertiesName.add(tuple2._1().getIdentifier());
            }));
        }
    }

    public S2CTeamPlayerDataPacket(ServerPlayer player, Type type){
        this(player, type, HashSet.empty());
    }



    public S2CTeamPlayerDataPacket(FriendlyByteBuf byteBuf) {
        tag = byteBuf.readNbt();
        this.propertiesName = byteBuf.readList(FriendlyByteBuf::readUtf);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeNbt(tag);
        to.writeCollection(this.propertiesName, FriendlyByteBuf::writeUtf);
    }

    @Override
    public void handleClient() {
        UUID uuid = tag.getUUID(S2CTeamPlayerDataPacket.ID_KEY);
        Type type1 = Type.valueOf(tag.getString(S2CTeamPlayerDataPacket.TYPE_KEY));
        //System.out.println("handle on client, the type is " + type1);
        switch (type1) {
            case ADD -> {
                if (ClientTeam.INSTANCE.hasPlayer(uuid)) return;

                String name = tag.getString(S2CTeamPlayerDataPacket.NAME_KEY);

                // Get skin data
                String skinVal = tag.getString(S2CTeamPlayerDataPacket.SKIN_KEY);
                String skinSig = tag.getString(S2CTeamPlayerDataPacket.SKIN_SIG_KEY);

                // Force download
                if (!skinVal.isEmpty()) {
                    GameProfile dummy = new GameProfile(UUID.randomUUID(), "");
                    dummy.getProperties().put("textures", new Property("textures", skinVal, skinSig));
                    Minecraft.getInstance().getSkinManager().registerSkins(dummy, (type, id, texture) -> {
                        if (type == MinecraftProfileTexture.Type.SKIN) {

                            this.postAndDo(new ClientReadPropertyEvent(tag, ImmutableList.copyOf(this.propertiesName)), event -> ClientTeam.INSTANCE.addPlayer(uuid, name, id, event.getResults()));

                        }
                    }, false);
                } else {
                    this.postAndDo(new ClientReadPropertyEvent(tag, ImmutableList.copyOf(this.propertiesName)), event -> ClientTeam.INSTANCE.addPlayer(uuid, name, DefaultPlayerSkin.getDefaultSkin(uuid), event.getResults()));
                }
            }
            case UPDATE -> {
                //System.out.println("updating!");
                this.postAndDo(new ClientReadPropertyEvent(tag, ImmutableList.copyOf(this.propertiesName)), event -> ClientTeam.INSTANCE.updatePlayer(uuid, event.getResults()));
            }
            case REMOVE -> {
                ClientTeam.INSTANCE.removePlayer(uuid);
            }
        }
    }

    private <T extends Event> void postAndDo(T event, Consumer<T> handle){
        if (event instanceof ServerEvent){
            handle.accept(AdvancedTeam.serverBus.post(event));
        } else if (event instanceof ClientEvent){
            handle.accept(AdvancedTeam.clientBus.post(event));
        }
    }

}
