package me.waterarchery.litlibs.hooks.protection;

import dev.espi.protectionstones.PSPlayer;
import dev.espi.protectionstones.PSRegion;
import dev.espi.protectionstones.event.PSRemoveEvent;
import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.events.LitIslandDisbandEvent;
import me.waterarchery.litlibs.hooks.ProtectionHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtectionStonesHook implements ProtectionHook, Listener {

    private static ProtectionStonesHook instance;

    public static synchronized ProtectionStonesHook getInstance() {
        if (instance == null) {
            instance = new ProtectionStonesHook();
        }
        return instance;
    }

    private ProtectionStonesHook() {
        Bukkit.getServer().getPluginManager().registerEvents(this, LitLibsPlugin.getInstance());
    }

    @EventHandler
    public void onRegionRemove(PSRemoveEvent event) {
        PSRegion region = event.getRegion();
        List<UUID> members = region.getMembers();

        LitIslandDisbandEvent litEvent = new LitIslandDisbandEvent(null, members);
        Bukkit.getServer().getPluginManager().callEvent(litEvent);
    }

    @Override
    public boolean canPlayerBuild(Location loc, Player p) {
        PSRegion region = PSRegion.fromLocation(loc);
        if (region != null) {
            return (region.isMember(p.getUniqueId()) || region.isOwner(p.getUniqueId()));
        }

        return false;
    }

    @Override
    public boolean hasProtection(Player player) {
        PSPlayer psPlayer = PSPlayer.fromPlayer(player);
        return !psPlayer.getPSRegions(player.getWorld(), true).isEmpty();
    }

    @Override
    public boolean isInternalChatEnabled(Player player) {
        return false;
    }

    @Override
    public Location getCenter(Location loc) {
        PSRegion region = PSRegion.fromLocation(loc);
        if (region != null) {
            return region.getProtectBlock().getLocation();
        }

        return null;
    }

    @Override
    public boolean isInside(Location loc) {
        return PSRegion.fromLocation(loc) != null;
    }

    @Override
    public ArrayList<UUID> getMembers(Location loc) {
        PSRegion region = PSRegion.fromLocation(loc);
        return region != null ? new ArrayList<>(region.getMembers()) : new ArrayList<>();
    }

    @Override
    public @Nullable UUID getIslandUUID(Location loc) {
        PSRegion region = PSRegion.fromLocation(loc);
        return region != null ? UUID.fromString(region.getId()) : null;
    }

    @Override
    public @Nullable UUID getIslandUUID(Player player) {
        PSPlayer psPlayer = PSPlayer.fromPlayer(player);
        List<PSRegion> regions = psPlayer.getPSRegions(player.getWorld(), true);
        if (!regions.isEmpty()) return UUID.fromString(regions.getFirst().getId());

        return null;
    }

    @Override
    public UUID getOwner(Location loc) {
        PSRegion region = PSRegion.fromLocationGroup(loc);
        if (region != null) {
            return region.getLandlord();
        }

        return null;
    }
}
