package me.waterarchery.litlibs.libs;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import me.waterarchery.litlibs.logger.Logger;
import me.waterarchery.litlibs.version.Version;
import me.waterarchery.litlibs.version.VersionHandler;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleAPI {

    private static ParticleNativeAPI api;

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

        // Logger.logMessage("Particle not found: " + particleName);
    }

}
