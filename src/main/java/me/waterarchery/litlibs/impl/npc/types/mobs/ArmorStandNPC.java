package me.waterarchery.litlibs.impl.npc.types.mobs;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.impl.npc.types.MobNPC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter @Setter
public class ArmorStandNPC extends MobNPC {

    private boolean isSmall;
    private boolean hasArms;
    private boolean hasNoBasePlate;

    public ArmorStandNPC(String name, String worldName, double x, double y, double z, Consumer<Player> onClickAction) {
        super(name, worldName, x ,y, z, EntityTypes.ARMOR_STAND, onClickAction);
    }

    public void moveArm(Vector3f angle) {
        List<EntityData> entityDataList = new ArrayList<>();
        EntityData handData = new EntityData(isOldVersion() ? 18 : 19, EntityDataTypes.ROTATION, angle);
        entityDataList.add(handData);

        WrapperPlayServerEntityMetadata handPacket = new WrapperPlayServerEntityMetadata(
                entityId,
                entityDataList
        );

        queuePacketSeeing(handPacket);
    }

    public void setSmall(boolean value) {
        this.isSmall = value;

        setAttributes();
    }

    public void setHasNoBasePlate(boolean value) {
        this.hasNoBasePlate = value;

        setAttributes();
    }

    public void setHasArms(boolean value) {
        this.hasArms = value;

        setAttributes();
    }

    public void setAttributes() {
        List<EntityData> entityDataList = new ArrayList<>();
        EntityData data = new EntityData(oldVersion ? 14 : 15, EntityDataTypes.BYTE, (byte) (0x01 | 0x04 | 0x08));
        entityDataList.add(data);

        WrapperPlayServerEntityMetadata modifyPacket = new WrapperPlayServerEntityMetadata(
                entityId,
                entityDataList
        );

        queuePacketSeeing(modifyPacket);
    }

    @Override
    public void update() {
        super.update();

        setAttributes();
    }

}
