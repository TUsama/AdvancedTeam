package com.clefal.teams.server.team;

import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.clefal.teams.config.ATServerConfig;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.clefal.teams.utils.ExtraCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;

import javax.annotation.Nullable;

@Getter
public class VanillaTeamAttachment implements IATServerTeamAttachment<VanillaTeamAttachment>, IPostProcess {

    public static final String IDENTIFIER = "vanilla_team";
    public static Codec<VanillaTeamAttachment> CODEC = RecordCodecBuilder.create(vanillaTeamAttachmentInstance -> vanillaTeamAttachmentInstance.group(
                    Codec.STRING.fieldOf("name").forGetter(x -> x.teamName),
                    VanillaTeamConfig.CODEC.fieldOf("config").forGetter(x -> x.config)
            ).apply(vanillaTeamAttachmentInstance, VanillaTeamAttachment::new)
    );
    @Nullable
    private PlayerTeam vanillaTeam;
    private String teamName;
    private VanillaTeamConfig config;
    public boolean isProcessed = false;

    public VanillaTeamAttachment(String teamName, VanillaTeamConfig config) {
        this.teamName = teamName;
        this.config = config;
    }

    public VanillaTeamAttachment(String teamName) {
        this.teamName = teamName;
        this.config = VanillaTeamConfig.EMPTY;
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Codec<VanillaTeamAttachment> getCodec() {
        return CODEC;
    }


    @Override
    public void postProcess(ServerLevel level, ATServerTeamData data, ATServerTeam team) {
        ServerScoreboard scoreboard = level.getScoreboard();
        vanillaTeam = scoreboard.getPlayerTeam(teamName);
        if (vanillaTeam == null) {
            vanillaTeam = scoreboard.addPlayerTeam(teamName);
        }
        if (config == VanillaTeamConfig.EMPTY){
            config = VanillaTeamConfig.fromTeam(vanillaTeam);
        } else {
            vanillaTeam.setColor(config.color);
            vanillaTeam.setCollisionRule(Team.CollisionRule.byName(config.collision));
            vanillaTeam.setAllowFriendlyFire(config.friendlyFire);
            vanillaTeam.setSeeFriendlyInvisibles(config.showInvisible);
            vanillaTeam.setDeathMessageVisibility(Team.Visibility.byName(config.deathMessages));
            vanillaTeam.setNameTagVisibility(Team.Visibility.byName(config.nameTags));
        }
        isProcessed = true;
    }

    public record VanillaTeamConfig(ChatFormatting color, String collision, String deathMessages, String nameTags,
                                    boolean friendlyFire, boolean showInvisible) {
        public static final VanillaTeamConfig EMPTY = new VanillaTeamConfig(ChatFormatting.WHITE, "", "", "", false, false);
        public static Codec<VanillaTeamConfig> CODEC = RecordCodecBuilder.create(vanillaTeamConfigInstance -> vanillaTeamConfigInstance.group(
                        ExtraCodecs.CHATFORMATTING.fieldOf("color").forGetter(x -> x.color),
                        Codec.STRING.fieldOf("col").forGetter(VanillaTeamConfig::collision),
                        Codec.STRING.fieldOf("dm").forGetter(VanillaTeamConfig::deathMessages),
                        Codec.STRING.fieldOf("nt").forGetter(VanillaTeamConfig::nameTags),
                        Codec.BOOL.fieldOf("ff").forGetter(VanillaTeamConfig::friendlyFire),
                        Codec.BOOL.fieldOf("si").forGetter(VanillaTeamConfig::showInvisible)
                ).apply(vanillaTeamConfigInstance, VanillaTeamConfig::new)
        );

        public static VanillaTeamConfig fromTeam(PlayerTeam team) {
            return new VanillaTeamConfig(team.getColor()
                    , team.getCollisionRule().name
                    , team.getDeathMessageVisibility().name
                    , team.getNameTagVisibility().name
                    , team.isAllowFriendlyFire()
                    , team.canSeeFriendlyInvisibles());

        }

    }
}
