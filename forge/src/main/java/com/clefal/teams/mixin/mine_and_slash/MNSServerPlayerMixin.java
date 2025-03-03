package com.clefal.teams.mixin.mine_and_slash;

import com.clefal.teams.utils.MixinHelper;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class MNSServerPlayerMixin {

    @Inject(at = @At(value = "HEAD"), method = "tick")
    private void tickJob(CallbackInfo ci) {
        MixinHelper.updateStatusEffects(self());
    }

    private ServerPlayer self() {
        return (ServerPlayer) ((Object) this);
    }

}
