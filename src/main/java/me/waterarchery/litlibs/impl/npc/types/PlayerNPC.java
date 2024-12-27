package me.waterarchery.litlibs.impl.npc.types;

import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.impl.npc.NPC;
import org.bukkit.Location;

@Getter
@Setter
public class PlayerNPC extends NPC {

    private String skinProfile;

    public PlayerNPC(String name, Location location, Runnable onClickAction, String skinProfile) {
        super(name, location, onClickAction);

        this.skinProfile = skinProfile;
    }

}
