package me.waterarchery.litlibs.hooks.protection;

import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.hooks.ProtectionHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

import java.util.ArrayList;
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
        if (island.isPresent()) {
            String rawUUID = island.get().getUniqueId().replace("BSkyBlock", "");
            return UUID.fromString(rawUUID);
        }
        return null;
    }

    @Override
    public @Nullable UUID getIslandUUID(Player player) {
        Island island = BentoBox.getInstance().getIslandsManager().getIsland(player.getWorld(), player.getUniqueId());
        if (island != null) {
            String rawUUID = island.getUniqueId().replace("BSkyBlock", "");
            return UUID.fromString(rawUUID);
        }
        return null;
    }

    @Override
    public @Nullable UUID getOwner(Location loc) {
        Optional<Island> island = BentoBox.getInstance().getIslands().getIslandAt(loc);
        return island.map(Island::getOwner).orElse(null);
    }

    @Override
    public ArrayList<UUID> getMembers(Location loc) {
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

}
