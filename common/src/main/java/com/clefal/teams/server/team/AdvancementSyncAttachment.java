package com.clefal.teams.server.team;

import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.clefal.teams.config.ATServerConfig;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.clefal.teams.utils.ExtraCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.HashSet;
import java.util.Set;
@Getter
public class AdvancementSyncAttachment implements IATServerTeamAttachment<AdvancementSyncAttachment>, IPostProcess {
    public static String IDENTIFIER = "advancement_sync";
    public Set<ResourceLocation> advancementLocations;
    private Set<Advancement> advancements;
    public static Codec<AdvancementSyncAttachment> CODEC = RecordCodecBuilder.create(advancementSyncAttachmentInstance -> advancementSyncAttachmentInstance.group(
            ExtraCodecs.transformToSet(ResourceLocation.CODEC.listOf()).fieldOf("locations").forGetter(x -> x.advancementLocations)
    ).apply(advancementSyncAttachmentInstance, AdvancementSyncAttachment::new)
    );

    public AdvancementSyncAttachment(Set<ResourceLocation> advancementLocations) {
        this.advancementLocations = advancementLocations;
        this.advancements = new HashSet<>();
    }

    public AdvancementSyncAttachment() {
        this.advancementLocations = new HashSet<>();
        this.advancements = new HashSet<>();
    }

    public void addAdvancement(Advancement advancement) {
        this.advancements.add(advancement);
        this.advancementLocations.add(advancement.getId());
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Codec<AdvancementSyncAttachment> getCodec() {
        return CODEC;
    }

    @Override
    public void postProcess(ServerLevel level, ATServerTeamData data, ATServerTeam team) {
        for (ResourceLocation advancementLocation : advancementLocations) {
            if (advancementLocation == null) continue;
            Advancement advancement = level.getServer().getAdvancements().getAdvancement(advancementLocation);
            if (advancement != null) {
                addAdvancement(advancement);
            }
        }

    }
}
