package me.waterarchery.litlibs.impl.npc.types;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.impl.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@Getter
@Setter
public class PlayerNPC extends NPC {

    private String skinProfile;

    public PlayerNPC(String name, Location location, Consumer<Player> onClickAction, String skinProfile) {
        super(name, location, EntityTypes.PLAYER, onClickAction);

        this.skinProfile = skinProfile;
    }

}
