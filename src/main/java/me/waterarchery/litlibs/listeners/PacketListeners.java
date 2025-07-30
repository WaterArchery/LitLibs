package me.waterarchery.litlibs.listeners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.tcoded.folialib.FoliaLib;
import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.handlers.NPCHandler;
import me.waterarchery.litlibs.impl.npc.NPC;
import org.bukkit.entity.Player;

import java.util.*;

public class PacketListeners implements PacketListener {

    private final List<UUID> recentlyClicked = new ArrayList<>();

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;

        NPCHandler npcHandler = NPCHandler.getInstance();
        WrapperPlayClientInteractEntity eventWrapper = new WrapperPlayClientInteractEntity(event);
        Player player = event.getPlayer();
        int entityId = eventWrapper.getEntityId();

        if (player == null) return;
        if (recentlyClicked.contains(player.getUniqueId())) return;
        recentlyClicked.add(player.getUniqueId());

        LitLibsPlugin plugin = LitLibsPlugin.getInstance();
        FoliaLib foliaLib = plugin.getFoliaLib();
        foliaLib.getScheduler().runLater(() -> recentlyClicked.remove(player.getUniqueId()), 5);

        List<NPC> clone = new ArrayList<>(npcHandler.getNpcs());
        clone.stream().filter(Objects::nonNull).filter(npc -> npc.getEntityId() == entityId).forEach(npc -> npc.execute(player));
    }

}
