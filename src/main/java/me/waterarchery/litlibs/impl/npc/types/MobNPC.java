package me.waterarchery.litlibs.impl.npc.types;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.impl.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@Getter
@Setter
public class MobNPC extends NPC {

    public MobNPC(String name, Location location, EntityType entityType, Consumer<Player> onClickAction) {
        super(name, location, entityType, onClickAction);
    }

}
