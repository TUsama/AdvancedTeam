package com.clefal.teams.mixin;

import com.clefal.teams.mixinhelper.Hooker;
import com.clefal.teams.server.IHasTeam;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public class UpdateEffectMixin {

    @Inject(method = "addAdditionalSaveData",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;put(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;", ordinal = 1
    ),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void update(CompoundTag compound, CallbackInfo ci, ListTag listtag) {
        Hooker.updateEffects(listtag, (LivingEntity) (Object)this);
    }


}
