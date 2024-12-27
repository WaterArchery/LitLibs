package me.waterarchery.litlibs.impl.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.LitLibsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public abstract class NPC {

    private final UUID uuid;
    private final int entityId;
    private List<UUID> seeingPlayers;
    private String name;
    private Location location;
    private BukkitTask updateSeeingTask;
    private Runnable onClickAction;

    public NPC(String name, Location location, Runnable onClickAction) {
        this.uuid = UUID.randomUUID();
        this.entityId = SpigotReflectionUtil.generateEntityId();
        this.name = name;
        this.seeingPlayers = new ArrayList<>();
        this.onClickAction = onClickAction;
    }

    public void startSeeingTask() {
        if (updateSeeingTask != null) {
            seeingPlayers.clear();
            updateSeeingTask.cancel();
        }

        updateSeeingTask = new BukkitRunnable() {
            @Override
            public void run() {
                World world = location.getWorld();
                if (world == null) return;

                List<UUID> newSeeingList = new ArrayList<>();
                world.getNearbyEntities(location, 32, 32, 32, (e) -> e.getType() == EntityType.PLAYER)
                        .forEach(player -> {
                            newSeeingList.add(player.getUniqueId());

                            if (!seeingPlayers.contains(player.getUniqueId())) {
                                // Seeing for the first time
                                seeingPlayers.add(player.getUniqueId());
                                spawn((Player) player);
                            }

                            seeingPlayers = newSeeingList;
                        });
            }
        }.runTaskTimer(LitLibsPlugin.getInstance(), 20, 20);
    }

    public void spawn(Player player) {
        com.github.retrooper.packetevents.protocol.world.Location spawnLocation = SpigotConversionUtil.fromBukkitLocation(location);

        WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(
                entityId,
                uuid,
                EntityTypes.ARMOR_STAND,
                spawnLocation,
                location.getYaw(),
                0,
                null
        );

        List<EntityData> entityDataList = new ArrayList<>();
        EntityData data2 = new EntityData(15, EntityDataTypes.BYTE, (byte) (0x01 | 0x04 | 0x08));
        entityDataList.add(data2);
        WrapperPlayServerEntityMetadata modifyPacket = new WrapperPlayServerEntityMetadata(
                entityId,
                entityDataList
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, spawnPacket);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, modifyPacket);
        update();
    }

    public void update() {
        updateRotation();
        updateEquipment();
    }

    public void updateEquipment() {
        List<Equipment> equipments = new ArrayList<>();
        WrapperPlayServerEntityEquipment equipmentPacket = new WrapperPlayServerEntityEquipment(
                entityId,
                equipments
        );

        broadcastSeeing(equipmentPacket);
    }

    public void updateRotation() {
        float yaw = location.getYaw();
        float pitch = location.getPitch();

        WrapperPlayServerEntityRotation packet = new WrapperPlayServerEntityRotation(
                entityId,
                yaw,
                pitch,
                false
        );

        broadcastSeeing(packet);
    }

    public void despawn() {
        WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(
                entityId
        );

        broadcastSeeing(packet);
    }

    public void broadcastSeeing(PacketWrapper<?> packet) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            UUID playerUUID = player.getUniqueId();

            if (seeingPlayers.contains(playerUUID)) PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
        });
    }

    public void broadcast(PacketWrapper<?> packet) {
        Bukkit.getOnlinePlayers()
                .forEach(player -> PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet));
    }

}
