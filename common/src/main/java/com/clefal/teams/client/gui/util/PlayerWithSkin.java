package com.clefal.teams.client.gui.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public record PlayerWithSkin(String name, UUID uuid, String value, String signature) {

    public ResourceLocation getSkin() {
        AtomicReference<ResourceLocation> result = new AtomicReference<>();
        if (!this.value().isEmpty()) {
            GameProfile dummy = new GameProfile(UUID.randomUUID(), "");
            dummy.getProperties().put("textures", new Property("textures", value(), signature()));
            Minecraft.getInstance().getSkinManager().registerSkins(dummy, (type, id, texture) -> {
                if (type == MinecraftProfileTexture.Type.SKIN) {
                    result.set(id);
                } else {
                    result.set(DefaultPlayerSkin.getDefaultSkin(uuid));
                }
            }, false);
        } else {
            result.set(DefaultPlayerSkin.getDefaultSkin(uuid));
        }

        return result.get();

    }
}
