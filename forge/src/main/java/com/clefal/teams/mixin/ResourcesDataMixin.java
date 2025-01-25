package com.clefal.teams.mixin;

import com.clefal.teams.mixinhelper.Hooker;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourceType;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourcesData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ResourcesData.class)
public class ResourcesDataMixin {
    @Inject(
            at = @At(value = "RETURN"),
            method = "sync",
    remap = false)
    private void update(LivingEntity en, CallbackInfo ci){
        if (en instanceof ServerPlayer player) {
            Hooker.UpdatePropertyInfoForEveryone(player);
        }
    }
}
