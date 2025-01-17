package com.clefal.teams.client.core;

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
        private final Map<String, IRenderableProperty> properties;

        Teammate(UUID id, String name, ResourceLocation skin, float health, int hunger) {
            this.id = id;
            this.name = name;
            this.skin = skin;
            this.properties = new LinkedHashMap<>();
            Health health1 = new Health(health);
            Hunger hunger1 = new Hunger(hunger);
            this.properties.put(health1.getIdentifier(), health1);
            this.properties.put(hunger1.getIdentifier(), hunger1);
        }

        Teammate(UUID id, String name, ResourceLocation skin, Map<String, IRenderableProperty> properties) {
            this.id = id;
            this.name = name;
            this.skin = skin;
            this.properties = properties;
        }

        public float getHealth() {
            return Float.parseFloat(this.properties.get(Health.KEY).getRenderString());
        }

        public int getHunger() {
            return Integer.parseInt(this.properties.get(Hunger.KEY).getRenderString());
        }

        public void addProperty(IRenderableProperty property){
            properties.put(property.getIdentifier(), property);
        }


        public void removeProperty(ResourceLocation resourceLocation){
            this.properties.remove(resourceLocation);
        }
    }

}
