package me.waterarchery.litlibs.handlers;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.impl.npc.NPC;
import me.waterarchery.litlibs.utils.ChunkUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.concurrent.*;

@Getter
@Setter
public class NPCHandler {

    private static NPCHandler instance;
    private final List<NPC> npcs = new ArrayList<>();
    private final ThreadFactory namedThreadFactory;
    private final ExecutorService executor = Executors.newFixedThreadPool(1, r -> {
        Thread t = new Thread(r);
        t.setUncaughtExceptionHandler((thread, throwable) -> throwable.printStackTrace());
        return t;
    });
    private WrappedTask updateTask;

    public static NPCHandler getInstance() {
        if (instance == null) instance = new NPCHandler();

        return instance;
    }

    private NPCHandler() {
        namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("litlibs-%d").build();

        startUpdateTask();
    }

    public void startUpdateTask() {
        if (updateTask != null) updateTask.cancel();

        LitLibsPlugin plugin = LitLibsPlugin.getInstance();
        FoliaLib foliaLib = plugin.getFoliaLib();
        updateTask = foliaLib.getScheduler().runTimerAsync(() -> {
            for (NPC npc : new ArrayList<>(npcs)) {
                if (npc == null) {
                    npcs.remove(null);
                    continue;
                }
                if (npc.isDespawned()) continue;

                Location location = npc.getLocation();
                if (!location.isWorldLoaded()) continue;
                if (!ChunkUtils.isChunkLoaded(location.getWorld(), location.getBlockX() / 16, location.getBlockZ() / 16)) continue;

                World world = location.getWorld();
                if (world == null) continue;

                List<UUID> clone = new ArrayList<>(npc.getSeeingPlayers());
                clone.forEach(playerUUID -> {
                    Player player = Bukkit.getPlayer(playerUUID);
                    if (player == null) {
                        npc.getSeeingPlayers().remove(playerUUID);
                        return;
                    }

                    Location playerLocation = player.getLocation();
                    if (!Objects.equals(playerLocation.getWorld(), world)) npc.despawn(player, false);
                });

                List<UUID> newSeeingList = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().startsWith("Loader-")) continue; // Wild Loaders
                    if (!Objects.equals(player.getLocation().getWorld(), location.getWorld())) continue;
                    if (player.getLocation().distance(location) > 31) {
                        if (npc.getSeeingPlayers().contains(player.getUniqueId())) npc.despawn(player, false);
                        continue;
                    }

                    newSeeingList.add(player.getUniqueId());
                    if (!npc.getSeeingPlayers().contains(player.getUniqueId())) npc.spawn(player);
                }

                npc.setSeeingPlayers(new ArrayList<>(newSeeingList));
            }
        }, 0, 10);
    }

    public void deleteNPC(UUID uuid) {
        List<NPC> deletedNpcs = new ArrayList<>(npcs).stream().filter(npc -> npc != null && npc.getUuid().equals(uuid)).toList();
        deletedNpcs.forEach(npc -> {
            npcs.remove(npc);

            if (npc == null) return;
            npc.despawn();
        });
    }

    public Team getColorTeam(ChatColor color) {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        String teamName = color.toString() + "_LitNPC";
        Team team = board.getTeam(teamName);

        if (team == null) {
            team = board.registerNewTeam(teamName);
            team.setColor(color);
        }

        return team;
    }

}
