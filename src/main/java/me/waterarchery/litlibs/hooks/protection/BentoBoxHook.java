package me.waterarchery.litlibs.hooks.protection;

import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.events.LitIslandDisbandEvent;
import me.waterarchery.litlibs.events.LitIslandKickEvent;
import me.waterarchery.litlibs.hooks.ProtectionHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.events.island.IslandDeleteEvent;
import world.bentobox.bentobox.api.events.team.TeamKickEvent;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.database.objects.Players;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BentoBoxHook implements ProtectionHook, Listener {

    private static BentoBoxHook instance = null;

    public static synchronized BentoBoxHook getInstance() {
        if (instance == null) {
            instance = new BentoBoxHook();
        }
        return instance;
    }

    private BentoBoxHook() {
        Bukkit.getServer().getPluginManager().registerEvents(this, LitLibsPlugin.getInstance());
    }

    @Override
    public @Nullable UUID getIslandUUID(Location loc) {
        Optional<Island> island = BentoBox.getInstance().getIslands().getIslandAt(loc);
        return island.map(value -> parseUUID(value.getUniqueId())).orElse(null);
    }

    @Override
    public @Nullable UUID getIslandUUID(Player player) {
        Island island = BentoBox.getInstance().getIslandsManager().getIsland(player.getWorld(), player.getUniqueId());
        if (island != null) {
            return parseUUID(island.getUniqueId());
        }
        return null;
    }

    @Override
    public @Nullable UUID getOwner(Location loc) {
        Optional<Island> island = BentoBox.getInstance().getIslands().getIslandAt(loc);
        return island.map(Island::getOwner).orElse(null);
    }

    @Override
    public List<UUID> getMembers(Location loc) {
        Optional<Island> island = BentoBox.getInstance().getIslands().getIslandAt(loc);
        ArrayList<UUID> members = new ArrayList<>();
        island.ifPresent(value -> members.addAll(value.getMemberSet()));

        return members;
    }

    @Override
    public boolean canPlayerBuild(Location loc, Player player) {
        Optional<Island> island = BentoBox.getInstance().getIslands().getIslandAt(loc);
        return island.filter(value -> value.getMemberSet().contains(player.getUniqueId())
                || value.getOwner().equals(player.getUniqueId())).isPresent();
    }

    @Override
    public boolean hasProtection(Player player) {
        Island island = BentoBox.getInstance().getIslandsManager().getIsland(player.getWorld(), player.getUniqueId());
        return island != null;
    }

    @Override
    public boolean isInternalChatEnabled(Player player) {
        Players players = BentoBox.getInstance().getPlayers().getPlayer(player.getUniqueId());
        return false; // I couldn't find the proper boolean :(
    }

    @Override
    public Location getCenter(Location loc) {
        Island island = BentoBox.getInstance()
                .getIslandsManager()
                .getIslandAt(loc)
                .orElse(null);

        return island != null ? island.getCenter() : null;
    }

    @Override
    public boolean isInside(Location loc) {
        Island island = BentoBox.getInstance()
                .getIslandsManager()
                .getIslandAt(loc)
                .orElse(null);

        return island != null && island.inIslandSpace(loc);
    }

    @EventHandler
    public void onIslandDisband(IslandDeleteEvent e) {
        Island island = e.getIsland();
        List<UUID> members = new ArrayList<>(island.getMemberSet());

        LitIslandDisbandEvent litEvent = new LitIslandDisbandEvent(parseUUID(island.getUniqueId()), members);
        Bukkit.getServer().getPluginManager().callEvent(litEvent);
    }

    @EventHandler
    public void onIslandKick(TeamKickEvent e) {
        Island island = e.getIsland();
        UUID uuid = e.getPlayerUUID();

        LitIslandKickEvent litEvent = new LitIslandKickEvent(parseUUID(island.getUniqueId()), uuid, null);
        Bukkit.getServer().getPluginManager().callEvent(litEvent);
    }

    private UUID parseUUID(String rawUUID) {
        String cleanedUUID = rawUUID.replace("BSkyBlock", "");
        return UUID.fromString(cleanedUUID);
    }
}
