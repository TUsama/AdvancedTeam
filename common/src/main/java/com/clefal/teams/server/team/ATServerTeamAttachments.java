package com.clefal.teams.server.team;

import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.clefal.teams.config.ATServerConfig;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import net.minecraft.server.level.ServerLevel;

@AllArgsConstructor
public class ATServerTeamAttachments implements IPostProcess {
    public static Codec<ATServerTeamAttachments> CODEC = RecordCodecBuilder.create(atServerTeamAttachmentsInstance -> atServerTeamAttachmentsInstance.group(
                    VanillaTeamAttachment.CODEC.fieldOf("vta").forGetter(x -> x.vanillaTeamAttachment),
                    AdvancementSyncAttachment.CODEC.fieldOf("asa").forGetter(x -> x.advancementSyncAttachment)
            ).apply(atServerTeamAttachmentsInstance, ATServerTeamAttachments::new)
    );
    private VanillaTeamAttachment vanillaTeamAttachment;
    private AdvancementSyncAttachment advancementSyncAttachment;

    public Option<AdvancementSyncAttachment> getAdvancementSyncAttachment() {
        return advancementSyncAttachment.isProcessed ? Option.some(advancementSyncAttachment) : Option.none();
    }

    public Option<VanillaTeamAttachment> getVanillaTeamAttachment() {
        return vanillaTeamAttachment.isProcessed ? Option.some(vanillaTeamAttachment) : Option.none();
    }


    @Override
    public void postProcess(ServerLevel level, ATServerTeamData data, ATServerTeam team) {
        if (ATServerConfig.config.enableVanillaTeamCompat) vanillaTeamAttachment.postProcess(level, data, team);

        if (ATServerConfig.config.shareAchievements) advancementSyncAttachment.postProcess(level, data, team);
    }
}
