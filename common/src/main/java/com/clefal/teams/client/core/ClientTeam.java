package com.clefal.teams.client.core;

import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.clefal.teams.client.core.property.ITracking;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ClientTeam {

    ClientTeam INSTANCE = new ClientTeamImpl();

    void init(String name, UUID leader);

    String getName();

    boolean canInvite();
    void setCanInvite(boolean canInvite);

    void changeLeader(UUID leader);

    void updatePermission(boolean has);

    boolean hasPermissions();

    boolean isInTeam();

    boolean isTeamEmpty();

    List<Teammate> getTeammates();

    boolean hasPlayer(UUID player);

    void addPlayer(UUID player, String name, ResourceLocation skin, IProperty... others);

    void updatePlayer(UUID player, IProperty... properties);

    void removePlayer(UUID player);


    void reset();

    class Teammate {
        public final UUID id;
        public final String name;
        public final ResourceLocation skin;
        private final Map<String, IProperty> properties = new LinkedHashMap<>();


        Teammate(UUID id, String name, ResourceLocation skin, IProperty... properties) {
            this.id = id;
            this.name = name;
            this.skin = skin;
            for (IProperty property : properties) {
                this.properties.put(property.getIdentifier(), property);
            }
        }


        public void addProperty(IProperty property) {
            if (property instanceof ITracking tracking) {
                IProperty old = properties.get(property.getIdentifier());
                ITracking<?> o = (ITracking<?>) tracking.mergeWith(old);
                properties.put(property.getIdentifier(), (IProperty) o);
            } else {
                properties.put(property.getIdentifier(), property);
            }


        }

        public Option<IProperty> getProperty(String key) {
            return Option.of(this.properties.get(key));
        }

    }

}
