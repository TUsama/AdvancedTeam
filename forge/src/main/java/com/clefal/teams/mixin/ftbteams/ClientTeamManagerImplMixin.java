package com.clefal.teams.mixin.ftbteams;

import com.clefal.teams.utils.MixinHelper;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import dev.ftb.mods.ftbteams.data.ClientTeamManagerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(value = ClientTeamManagerImpl.class)
public class ClientTeamManagerImplMixin {

    @Shadow(remap = false)
    @Final
    private Map<UUID, KnownClientPlayer> knownPlayers;

    @Shadow(remap = false)
    private KnownClientPlayer selfKnownPlayer;

    @Inject(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), method = "initSelfDetails", cancellable = true, remap = false)
    private void init(CallbackInfo info) {
        MixinHelper.enableOfflineForFTBTeams(selfKnownPlayer, knownPlayers, info);
    }
}

