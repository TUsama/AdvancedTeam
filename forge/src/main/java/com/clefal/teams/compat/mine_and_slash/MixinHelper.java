package com.clefal.teams.compat.mine_and_slash;

import com.clefal.teams.compat.mine_and_slash.property.MNSStatusEffect;
import com.clefal.teams.server.IHasTeam;
import com.clefal.teams.server.IPropertySender;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import lombok.experimental.UtilityClass;
import net.minecraft.server.level.ServerPlayer;

@UtilityClass
public class MixinHelper {

    public void updateStatusEffects(ServerPlayer player){
        if (MineAndSlashCompatModule.INSTANCE.isEnabled){
            IPropertySender propertySender = (IPropertySender) player;
            IHasTeam hasTeam = (IHasTeam) player;
            if (hasTeam.hasTeam() && !Load.Unit(player).getStatusEffectsData().exileMap.isEmpty()){
                propertySender.addUpdate(MNSStatusEffect.KEY);
            }
        }
    }

}
