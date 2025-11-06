package me.waterarchery.litlibs.hooks.protection;

import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.events.LandDeleteEvent;
import me.angeschossen.lands.api.events.PlayerLeaveLandEvent;
import me.angeschossen.lands.api.land.Land;
import me.angeschossen.lands.api.player.LandPlayer;
import me.angeschossen.lands.api.player.chat.ChatMode;
import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.events.LitIslandDisbandEvent;
import me.waterarchery.litlibs.events.LitIslandLeaveEvent;
import me.waterarchery.litlibs.hooks.ProtectionHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LandsHook implements ProtectionHook, Listener {

    private static LandsHook instance = null;
    private final LandsIntegration landsIntegration;

    public static synchronized LandsHook getInstance() {
        if (instance == null) {
            instance = new LandsHook();
        }
        return instance;
    }

    private LandsHook() {
        landsIntegration = LandsIntegration.of(LitLibsPlugin.getInstance());
        Bukkit.getServer().getPluginManager().registerEvents(this, LitLibsPlugin.getInstance());
    }

    @Override
    public @Nullable UUID getIslandUUID(Location loc) {
        Land land = getLandAt(loc);
        return land != null ? land.getOwnerUID() : null; // Temp solution
    }

    @Override
    public @Nullable UUID getIslandUUID(Player player) {
        LandPlayer landPlayer = landsIntegration.getLandPlayer(player.getUniqueId());
        return landPlayer.getLands().isEmpty() ? null : landPlayer.getLands().iterator().next().getOwnerUID();
    }

    @Override
    public @Nullable UUID getOwner(Location loc) {
        Land land = getLandAt(loc);
        return land != null ? land.getOwnerUID() : null;
    }

    @Override
    public List<UUID> getMembers(Location loc) {
        List<UUID> members = new ArrayList<>();
        Land land = getLandAt(loc);

        if (land != null) members.addAll(land.getTrustedPlayers());
        return members;
    }

    @Override
    public boolean canPlayerBuild(Location loc, Player player) {
        List<UUID> members = getMembers(loc);
        UUID owner = getOwner(loc);

        if (owner != null) return members.contains(player.getUniqueId()) || owner.equals(player.getUniqueId());
        return false;
    }

    @Override
    public boolean hasProtection(Player player) {
        LandPlayer landPlayer = landsIntegration.getLandPlayer(player.getUniqueId());
        return !landPlayer.getLands().isEmpty();
    }

    @Override
    public boolean isInternalChatEnabled(Player player) {
        ChatMode mode = landsIntegration.getLandPlayer(player.getUniqueId()).getChatMode();
        return mode == ChatMode.LAND;
    }

    @Override
    public Location getCenter(Location loc) {
        Land land = getLandAt(loc);
        return land != null && land.getSpawnPosition() != null ? land.getSpawnPosition().toLocation() : null;
    }

    @Override
    public boolean isInside(Location loc) {
        Land land = getLandAt(loc);
        return land != null && land.getArea(loc) != null;
    }

    @EventHandler
    public void onLandDelete(LandDeleteEvent e) {
        Land land = e.getLand();
        List<UUID> members = new ArrayList<>(land.getTrustedPlayers());
        members.add(land.getOwnerUID());

        LitIslandDisbandEvent litEvent = new LitIslandDisbandEvent(land.getOwnerUID(), members);
        Bukkit.getServer().getPluginManager().callEvent(litEvent);
    }

    @EventHandler
    public void onLandLeave(PlayerLeaveLandEvent e) {
        Land land = e.getLand();
        LandPlayer player = e.getLandPlayer();

        LitIslandLeaveEvent litEvent = new LitIslandLeaveEvent(land.getOwnerUID(), player.getUID());
        Bukkit.getServer().getPluginManager().callEvent(litEvent);
    }

    private Land getLandAt(Location loc) {
        World world = loc.getWorld();
        if (world == null) return null;

        int chunkX = loc.getChunk().getX();
        int chunkZ = loc.getChunk().getZ();

        return landsIntegration.getLandByUnloadedChunk(world, chunkX, chunkZ);
    }
}
