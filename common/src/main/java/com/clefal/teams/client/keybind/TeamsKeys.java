package com.clefal.teams.client.keybind;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.client.gui.hud.CompassOverlay;
import com.clefal.teams.client.gui.hud.StatusOverlay;
import com.clefal.teams.network.server.C2SAcceptApplicationPacket;
import com.mojang.blaze3d.platform.InputConstants;
import com.clefal.teams.client.gui.toast.ToastInvited;
import com.clefal.teams.client.gui.toast.ToastApplied;
import com.clefal.teams.network.server.C2STeamJoinPacket;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.function.Consumer;

public class TeamsKeys {

    public static void registerAllKey(Consumer<KeyMapping[]> handle){
        handle.accept(Arrays.stream(KEYS).map(x -> x.keyBinding).toArray(KeyMapping[]::new));
    }

    public static void consumerKeys(){
        for (var key : KEYS) {
            if (key.keyBinding.consumeClick()) {
                key.onPress.execute(Minecraft.getInstance());
            }
        }
    }

    public static class TeamsKey {

        @FunctionalInterface
        public interface OnPress {
            void execute(Minecraft client);
        }

        private TeamsKey(String keyName, int keyBind, OnPress action) {
            keyBinding = new KeyMapping(
                    keyName,
                    InputConstants.Type.KEYSYM,
                    keyBind,
                    "key.category.teams"
            );
            onPress = action;
        }

        public void register(Consumer<KeyMapping> handle) {
            handle.accept(this.keyBinding);
        }

        public String getLocalizedName() {
            return keyBinding.getTranslatedKeyMessage().getString();
        }

        final KeyMapping keyBinding;
        final OnPress onPress;
    }

    public static final TeamsKey ACCEPT = new TeamsKey("key.teams.accept", GLFW.GLFW_KEY_RIGHT_BRACKET, client -> {
        var toastManager = client.getToasts();
        ToastInvited invited = toastManager.getToast(ToastInvited.class, Toast.NO_TOKEN);
        if (invited != null) {
            invited.respond();
            NetworkUtils.sendToServer(new C2STeamJoinPacket(invited.team));
        } else {
            ToastApplied requested = toastManager.getToast(ToastApplied.class, Toast.NO_TOKEN);
            if (requested != null) {
                requested.respond();
                NetworkUtils.sendToServer(new C2SAcceptApplicationPacket(requested.id));
            }
        }
    });

    public static final TeamsKey REJECT = new TeamsKey("key.teams.reject", GLFW.GLFW_KEY_LEFT_BRACKET, client -> {
        var toastManager = client.getToasts();
        ToastInvited toast = toastManager.getToast(ToastInvited.class, Toast.NO_TOKEN);
        if (toast != null) {
            toast.respond();
        } else {
            ToastApplied requested = toastManager.getToast(ToastApplied.class, Toast.NO_TOKEN);
            if (requested != null) {
                requested.respond();
            }
        }
    });

    public static final TeamsKey TOGGLE_HUD = new TeamsKey("key.teams.toggle_hud", GLFW.GLFW_KEY_B, client -> {
        StatusOverlay.INSTANCE.enabled = !StatusOverlay.INSTANCE.enabled;
        CompassOverlay.INSTANCE.enabled = !CompassOverlay.INSTANCE.enabled;
    });

    static final TeamsKey[] KEYS = {
            ACCEPT,
            REJECT,
            TOGGLE_HUD
    };

}
