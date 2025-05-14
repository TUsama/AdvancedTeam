package com.clefal.teams.server.team;

import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.clefal.teams.config.ATServerConfig;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.level.ServerLevel;

@Getter
@AllArgsConstructor
public class ATServerTeamAttachments implements IPostProcess{
    private VanillaTeamAttachment vanillaTeamAttachment;
    private AdvancementSyncAttachment advancementSyncAttachment;

    public static Codec<ATServerTeamAttachments> CODEC = RecordCodecBuilder.create(atServerTeamAttachmentsInstance -> atServerTeamAttachmentsInstance.group(
            VanillaTeamAttachment.CODEC.fieldOf("vta").forGetter(ATServerTeamAttachments::getVanillaTeamAttachment),
            AdvancementSyncAttachment.CODEC.fieldOf("asa").forGetter(ATServerTeamAttachments::getAdvancementSyncAttachment)
    ).apply(atServerTeamAttachmentsInstance, ATServerTeamAttachments::new)
    );

    public Option<VanillaTeamAttachment> conditionallyGetVanillaTeamAttachment(boolean condition){
        return condition ? Option.of(vanillaTeamAttachment) : Option.none();
    }

    public Option<AdvancementSyncAttachment> conditionallyGetAdvancementSyncAttachment(boolean condition){
        return condition ? Option.of(advancementSyncAttachment) : Option.none();
    }

    @Override
    public void postProcess(ServerLevel level, ATServerTeamData data, ATServerTeam team) {
        if ((ATServerConfig.config.enableVanillaTeamCompat.equals(ATServerConfig.Case.enable) && team.getConfig().enableVanillaTeamCompat) || ATServerConfig.config.enableVanillaTeamCompat.equals(ATServerConfig.Case.force)) vanillaTeamAttachment.postProcess(level, data, team);
        advancementSyncAttachment.postProcess(level, data, team);
    }
}
