package me.waterarchery.litlibs.handlers;

import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.impl.npc.types.MobNPC;
import me.waterarchery.litlibs.impl.npc.NPC;
import me.waterarchery.litlibs.impl.npc.types.PlayerNPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class NPCHandler {

    private static NPCHandler instance;
    final List<NPC> npcs = new ArrayList<>();

    public static NPCHandler getInstance() {
        if (instance == null) instance = new NPCHandler();

        return instance;
    }

    private NPCHandler() { }

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
