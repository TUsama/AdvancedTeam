package com.clefal.teams.mixin.mine_and_slash;

import com.clefal.teams.server.IHasTeam;
import com.clefal.teams.utils.MixinHelper;
import com.robertx22.library_of_exile.main.Packets;
import com.robertx22.mine_and_slash.a_libraries.dmg_number_particle.particle.InteractionNotifier;
import com.robertx22.mine_and_slash.vanilla_mc.packets.interaction.ExileInteractionResultPacket;
import com.robertx22.mine_and_slash.vanilla_mc.packets.interaction.IParticleSpawnMaterial;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InteractionNotifier.class)
public class InteractionNotifierMixin {

    @Inject(method = "notifyClient", at = @At(value = "RETURN"))
    private static void alsoSendToTeammate(IParticleSpawnMaterial notifier, ServerPlayer source, LivingEntity target, CallbackInfo ci){
        if (((IHasTeam) source).hasTeam()){
            MixinHelper.sendDMGParticleToTeammates(notifier, source, target);
        }
    }


}
