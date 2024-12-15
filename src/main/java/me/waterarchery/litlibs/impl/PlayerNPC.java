package me.waterarchery.litlibs.impl;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
@Setter
public class PlayerNPC extends NPC {

    private String skinProfile;

    public PlayerNPC(String name, Location location, Runnable onClickAction, String skinProfile) {
        super(name, location, onClickAction);

        this.skinProfile = skinProfile;
    }

}
