package me.waterarchery.litlibs.libs;

import me.waterarchery.litlibs.version.Version;
import me.waterarchery.litlibs.version.VersionHandler;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleAPI {

    public static void createParticle(Location location, String particleName, Player player) {
        // TODO IMPLEMENT PROPER PARTICLE API
        for (Particle particle : Particle.values()) {
            if (particle.name().equalsIgnoreCase(particleName)) {
                player.spawnParticle(particle, location, 0, 0, 0, 0, 1);
                return;
            }
        }

        if (particleName.equalsIgnoreCase("VILLAGER_ANGRY") && VersionHandler.getInstance().isServerNewerThan(Version.v1_20_5))
            player.spawnParticle(Particle.ANGRY_VILLAGER, location, 0, 0, 0, 0, 1);
    }

    public static void createParticle(Location location, String particleName) {
        // TODO IMPLEMENT PROPER PARTICLE API
        for (Particle particle : Particle.values()) {
            if (particle.name().equalsIgnoreCase(particleName)) {
                if (particle.name().contains("DUST")) {
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromARGB(255, 255, 255, 255), 1);
                    location.getWorld().spawnParticle(particle, location, 0, 0, 0, 0, 1, dustOptions);
                } else {
                    location.getWorld().spawnParticle(particle, location, 0, 0, 0, 0, 1);
                }
                return;
            }
        }

        if (particleName.equalsIgnoreCase("VILLAGER_ANGRY") && VersionHandler.getInstance().isServerNewerThan(Version.v1_20_5))
            location.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, location, 0, 0, 0, 0, 1);
    }

}
