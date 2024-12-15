package me.waterarchery.litlibs.impl;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

@Getter
@Setter
public class MobNPC extends NPC {

    private EntityType entityType;

    public MobNPC(String name, Location location, Runnable onClickAction, EntityType entityType) {
        super(name, location, onClickAction);

        this.entityType = entityType;
    }

}
