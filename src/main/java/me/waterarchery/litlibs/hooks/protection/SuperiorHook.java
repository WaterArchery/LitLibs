package me.waterarchery.litlibs.hooks.protection;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandKickEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandQuitEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.world.Dimension;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.events.LitIslandDisbandEvent;
import me.waterarchery.litlibs.events.LitIslandKickEvent;
import me.waterarchery.litlibs.events.LitIslandLeaveEvent;
import me.waterarchery.litlibs.hooks.ProtectionHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class SuperiorHook implements ProtectionHook, Listener {

    private static SuperiorHook instance = null;

    public static synchronized SuperiorHook getInstance() {
        if (instance == null) {
            instance = new SuperiorHook();
        }
        return instance;
    }

    private SuperiorHook() {
        Bukkit.getServer().getPluginManager().registerEvents(this, LitLibsPlugin.getInstance());
    }

    @Override
    public @Nullable UUID getIslandUUID(Location loc) {
        Island island = SuperiorSkyblockAPI.getIslandAt(loc);
        return island == null ? null : island.getUniqueId();
    }

    @Override
    public @Nullable UUID getIslandUUID(Player player) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        if (superiorPlayer != null) {
            Island island = superiorPlayer.getIsland();
            return island.getUniqueId();
        }

        return null;
    }

    @Override
    public @Nullable UUID getOwner(Location loc) {
        Island island = SuperiorSkyblockAPI.getIslandAt(loc);
        return island == null ? null : island.getOwner().getUniqueId();
    }

    @Override
    public List<UUID> getMembers(Location loc) {
        Island island = SuperiorSkyblockAPI.getIslandAt(loc);
        if (island != null) {
            return island.getIslandMembers(true)
                    .stream()
                    .map(SuperiorPlayer::getUniqueId)
                    .toList();
        }

        return List.of();
    }

    @Override
    public boolean canPlayerBuild(Location loc, Player player) {
        Island island = SuperiorSkyblockAPI.getIslandAt(loc);
        if (island != null) {
            SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
            return (island.getIslandMembers(true).contains(superiorPlayer) || island.getIslandMembers().contains(superiorPlayer) || island.getOwner()
                    .equals(superiorPlayer) || island.getCoopPlayers().contains(superiorPlayer));
        }

        return false;
    }

    @Override
    public boolean hasProtection(Player player) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        return superiorPlayer != null && superiorPlayer.getIsland() != null;
    }

    @Override
    public boolean isInternalChatEnabled(Player player) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        return superiorPlayer.hasTeamChatEnabled();
    }

    @Override
    public Location getCenter(Location loc) {
        Island island = SuperiorSkyblockAPI.getIslandAt(loc);
        if (island != null) return island.getCenter(Dimension.getByName(World.Environment.NORMAL.name()));
        return null;
    }

    @Override
    public boolean isInside(Location loc) {
        Island island = SuperiorSkyblockAPI.getIslandAt(loc);
        return island.isInside(loc);
    }

    @EventHandler
    public void onIslandDisband(IslandDisbandEvent e) {
        Island island = e.getIsland();
        List<UUID> members = island.getIslandMembers(true)
                .stream()
                .map(SuperiorPlayer::getUniqueId)
                .toList();

        LitIslandDisbandEvent litEvent = new LitIslandDisbandEvent(island.getUniqueId(), members);
        Bukkit.getServer().getPluginManager().callEvent(litEvent);
    }

    @EventHandler
    public void onIslandKick(IslandKickEvent e) {
        Island island = e.getIsland();
        Player kicker = e.getPlayer().asPlayer();
        UUID player = e.getTarget().getUniqueId();

        LitIslandKickEvent litEvent = new LitIslandKickEvent(island.getUniqueId(), player, kicker);
        Bukkit.getServer().getPluginManager().callEvent(litEvent);
    }

    @EventHandler
    public void onIslandLeave(IslandQuitEvent e) {
        Island island = e.getIsland();
        UUID player = e.getPlayer().getUniqueId();

        LitIslandLeaveEvent litEvent = new LitIslandLeaveEvent(island.getUniqueId(), player);
        Bukkit.getServer().getPluginManager().callEvent(litEvent);
    }
}
