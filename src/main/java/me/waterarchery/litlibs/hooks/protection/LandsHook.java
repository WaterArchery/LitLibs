package me.waterarchery.litlibs.hooks.protection;

import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.Land;
import me.angeschossen.lands.api.player.LandPlayer;
import me.angeschossen.lands.api.player.chat.ChatMode;
import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.hooks.ProtectionHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
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
        World world = loc.getWorld();
        int chunkX = loc.getChunk().getX();
        int chunkZ = loc.getChunk().getZ();

        Land land = landsIntegration.getLandByChunk(world, chunkX, chunkZ);
        return land != null ? land.getOwnerUID() : null; // Temp solution
    }

    @Override
    public @Nullable UUID getIslandUUID(Player player) {
        LandPlayer landPlayer = landsIntegration.getLandPlayer(player.getUniqueId());
        return landPlayer.getLands().isEmpty() ? null : landPlayer.getLands().iterator().next().getOwnerUID();
    }

    @Override
    public @Nullable UUID getOwner(Location loc) {
        World world = loc.getWorld();
        int chunkX = loc.getChunk().getX();
        int chunkZ = loc.getChunk().getZ();

        Land land = landsIntegration.getLandByChunk(world, chunkX, chunkZ);
        return land != null ? land.getOwnerUID() : null;
    }

    @Override
    public ArrayList<UUID> getMembers(Location loc) {
        ArrayList<UUID> members = new ArrayList<>();
        World world = loc.getWorld();
        int chunkX = loc.getChunk().getX();
        int chunkZ = loc.getChunk().getZ();

        Land land = landsIntegration.getLandByChunk(world, chunkX, chunkZ);
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

}
