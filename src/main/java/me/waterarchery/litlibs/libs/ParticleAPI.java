package me.waterarchery.litlibs.libs;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleAPI {

    private static ParticleNativeAPI api;

    public static void createParticle(Location location, String particleName, Player player) {
        // TODO IMPLEMENT PROPER PARTICLE API
        player.spawnParticle(Particle.valueOf(particleName), location, 0, 0, 0, 0, 1);
    }

}
