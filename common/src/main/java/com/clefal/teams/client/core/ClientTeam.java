package com.clefal.teams.client.core;

import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.clefal.teams.client.core.property.Health;
import com.clefal.teams.client.core.property.Hunger;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public interface ClientTeam {

    ClientTeam INSTANCE = new ClientTeamImpl();

    void init(String name, boolean hasPermissions);

    String getName();

    boolean hasPermissions();

    boolean isInTeam();

    boolean isTeamEmpty();

    List<Teammate> getTeammates();

    boolean hasPlayer(UUID player);

    void addPlayer(UUID player, String name, ResourceLocation skin, IRenderableProperty... others);

    void updatePlayer(UUID player, IRenderableProperty... properties);

    void removePlayer(UUID player);


    void reset();

    class Teammate {
        public final UUID id;
        public final String name;
        public final ResourceLocation skin;
        private final Map<String, IRenderableProperty> properties = new LinkedHashMap<>();


        Teammate(UUID id, String name, ResourceLocation skin, IRenderableProperty... properties) {
            this.id = id;
            this.name = name;
            this.skin = skin;
            for (IRenderableProperty property : properties) {
                this.properties.put(property.getIdentifier(), property);
            }
        }

        public float getHealth() {
            return Float.parseFloat(this.properties.get(Health.KEY).getRenderString());
        }

        public int getHunger() {
            return Integer.parseInt(this.properties.get(Hunger.KEY).getRenderString());
        }

        public void addProperty(IRenderableProperty property) {
            properties.put(property.getIdentifier(), property);
        }

        public Option<IRenderableProperty> getProperty(String key){
            return Option.of(this.properties.get(key));
        }

    }

}
