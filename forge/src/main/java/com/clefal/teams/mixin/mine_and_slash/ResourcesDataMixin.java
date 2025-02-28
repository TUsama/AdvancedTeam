package com.clefal.teams.mixin.mine_and_slash;

import com.clefal.teams.compat.mine_and_slash.property.MNSHealth;
import com.clefal.teams.compat.mine_and_slash.property.MNSMagicShield;
import com.clefal.teams.compat.mine_and_slash.property.MNSOtherResource;
import com.clefal.teams.mixinhelper.Hooker;
import com.clefal.teams.server.IPropertySender;
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
            method = "cap",
    remap = false)
    private void update(LivingEntity en, ResourceType type, CallbackInfo ci){
        if (en instanceof IPropertySender player) {
            if (type == ResourceType.health){
                player.addUpdate(MNSHealth.KEY);
            } else if (type == ResourceType.magic_shield) {
                player.addUpdate(MNSMagicShield.KEY);
            } else {
                player.addUpdate(MNSOtherResource.identifier.apply(type));
            }

        }
    }
}
