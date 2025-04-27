package me.waterarchery.litlibs.handlers;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Getter
@Setter
public class NPCHandler {

    private static NPCHandler instance;
    private final List<NPC> npcs = new ArrayList<>();
    private final ThreadFactory namedThreadFactory;
    private final ExecutorService executor;
    private BukkitTask updateTask;

    public static NPCHandler getInstance() {
        if (instance == null) instance = new NPCHandler();

        return instance;
    }

    private NPCHandler() {
        namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("litlibs-%d").build();
        executor = Executors.newSingleThreadExecutor(namedThreadFactory);

        startUpdateTask();
    }

    public void startUpdateTask() {
        if (updateTask != null) updateTask.cancel();

        updateTask = Bukkit.getScheduler().runTaskTimerAsynchronously(LitLibsPlugin.getInstance(), () -> {
            for (NPC npc : new ArrayList<>(npcs)) {
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
                    if (!player.getLocation().getWorld().equals(location.getWorld())) continue;
                    if (player.getLocation().distance(location) > 31) {
                        if (npc.getSeeingPlayers().contains(player.getUniqueId())) npc.despawn(player, false);
                        continue;
                    }

                    newSeeingList.add(player.getUniqueId());

                    if (!npc.getSeeingPlayers().contains(player.getUniqueId())) {
                        Bukkit.getScheduler().runTaskLaterAsynchronously(LitLibsPlugin.getInstance(), () -> npc.spawn(player), 5);
                    }
                }

                npc.setSeeingPlayers(new ArrayList<>(newSeeingList));
            }
        }, 0, 10);
    }

    public void deleteNPC(UUID uuid) {
        List<NPC> deletedNpcs = npcs.stream().filter(npc -> npc.getUuid().equals(uuid)).toList();
        deletedNpcs.forEach(npc -> {
            npcs.remove(npc);
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
