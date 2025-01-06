package com.clefal.teams.client.core;

import com.clefal.teams.TeamsHUD;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ClientTeam {

    ClientTeam INSTANCE = new ClientTeamImpl();

    void init(String name, boolean hasPermissions);

    String getName();

    boolean hasPermissions();

    boolean isInTeam();

    boolean isTeamEmpty();

    List<Teammate> getTeammates();

    boolean hasPlayer(UUID player);

    void addPlayer(UUID player, String name, ResourceLocation skin, float health, int hunger);

    void updatePlayer(UUID player, float health, int hunger);

    void removePlayer(UUID player);

    List<Teammate> getFavourites();

    boolean isFavourite(Teammate player);

    void addFavourite(Teammate player);

    void removeFavourite(Teammate player);

    void reset();

    class Teammate {
        public final UUID id;
        public final String name;
        public final ResourceLocation skin;
        private final Map<ResourceLocation, Number> properties;
        public static ResourceLocation HEALTH = TeamsHUD.id("textures/gui/health.png");
        public static ResourceLocation HUNGER = TeamsHUD.id("textures/gui/hunger.png");

        Teammate(UUID id, String name, ResourceLocation skin, float health, int hunger) {
            this.id = id;
            this.name = name;
            this.skin = skin;
            this.properties = new HashMap<>();
            this.properties.put(HEALTH, health);
            this.properties.put(HUNGER, hunger);
        }

        Teammate(UUID id, String name, ResourceLocation skin, Map<ResourceLocation, Number> properties) {
            this.id = id;
            this.name = name;
            this.skin = skin;
            this.properties = properties;
        }

        public float getHealth() {
            return this.properties.get(HEALTH).floatValue();
        }

        public int getHunger() {
            return this.properties.get(HUNGER).intValue();
        }

        public void addProperty(ResourceLocation resourceLocation, Number number){
            properties.put(resourceLocation, number);
        }

        public void addProperties(Map<ResourceLocation, Number> properties){
            this.properties.putAll(properties);
        }

        public void removeProperty(ResourceLocation resourceLocation){
            this.properties.remove(resourceLocation);
        }
    }

}
