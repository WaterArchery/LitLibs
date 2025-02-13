package me.waterarchery.litlibs.utils;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import org.bukkit.entity.EntityType;

import javax.annotation.Nullable;

public class NpcUtils {

    @Nullable
    public static com.github.retrooper.packetevents.protocol.entity.type.EntityType parseEntityType(EntityType bukkitType) {
        for (com.github.retrooper.packetevents.protocol.entity.type.EntityType type : EntityTypes.values()) {
            if (type.getName().getKey().equalsIgnoreCase(bukkitType.getName())) return type;
        }

        return null;
    }

}
