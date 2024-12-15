package me.waterarchery.litlibs.listeners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import me.waterarchery.litlibs.handlers.NPCHandler;

public class PacketListeners implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;

        NPCHandler npcHandler = NPCHandler.getInstance();
        WrapperPlayClientInteractEntity eventWrapper = new WrapperPlayClientInteractEntity(event);
        int entityId = eventWrapper.getEntityId();

        npcHandler.getNpcs().stream()
                .filter(npc -> npc.getEntityId() == entityId)
                .forEach(npc -> npc.getOnClickAction().run());
    }

}
