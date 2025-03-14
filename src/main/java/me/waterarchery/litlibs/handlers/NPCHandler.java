package me.waterarchery.litlibs.handlers;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.impl.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
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

    public static NPCHandler getInstance() {
        if (instance == null) instance = new NPCHandler();

        return instance;
    }

    private NPCHandler() {
        namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("litlibs-%d").build();
        executor = Executors.newCachedThreadPool(namedThreadFactory);
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
