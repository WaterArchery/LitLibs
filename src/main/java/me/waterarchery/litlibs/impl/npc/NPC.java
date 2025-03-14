package me.waterarchery.litlibs.impl.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litlibs.handlers.NPCHandler;
import me.waterarchery.litlibs.utils.ChunkUtils;
import me.waterarchery.litlibs.version.Version;
import me.waterarchery.litlibs.version.VersionHandler;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class NPC {

    private final UUID uuid;
    private List<Equipment> equipments;
    private EntityType entityType;
    protected final int entityId;
    protected final boolean oldVersion;
    protected List<UUID> seeingPlayers;
    protected String name;
    protected String worldName;
    protected double x;
    protected double y;
    protected double z;
    protected float yaw;
    protected float pitch;
    protected BukkitTask updateSeeingTask;
    protected Consumer<Player> onClickAction;
    protected final NPCHandler npcHandler;

    public NPC(String name, String worldName, double x, double y, double z, EntityType entityType, Consumer<Player> onClickAction) {
        this.uuid = UUID.randomUUID();
        this.entityId = SpigotReflectionUtil.generateEntityId();
        this.name = name;
        this.seeingPlayers = new ArrayList<>();
        this.onClickAction = onClickAction;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0;
        this.pitch = 0;
        this.equipments = new ArrayList<>();
        this.entityType = entityType;

        npcHandler = NPCHandler.getInstance();

        VersionHandler versionHandler = VersionHandler.getInstance();
        oldVersion = versionHandler.isServerOlderThan(Version.v1_17);

        npcHandler.getNpcs().add(this);
        startSeeingTask();
    }

    public void execute(Player player) {
        onClickAction.accept(player);
    }

    public void startSeeingTask() {
        if (updateSeeingTask != null) {
            seeingPlayers.clear();
            updateSeeingTask.cancel();
        }

        updateSeeingTask = new BukkitRunnable() {
            @Override
            public void run() {
                Location location = getLocation();

                if (!location.isWorldLoaded()) return;
                if (!ChunkUtils.isChunkLoaded(location.getWorld(), location.getBlockX() / 16, location.getBlockZ() / 16)) return;

                World world = location.getWorld();
                if (world == null) return;

                List<UUID> clone = new ArrayList<>(seeingPlayers);
                clone.forEach(playerUUID -> {
                    Player player = Bukkit.getPlayer(playerUUID);
                    if (player == null) {
                        seeingPlayers.remove(playerUUID);
                        return;
                    }

                    Location playerLocation = player.getLocation();
                    if (playerLocation.getWorld() != world) despawn(player, false);
                    else if (playerLocation.distance(location) > 32) despawn(player, false);
                });

                List<UUID> newSeeingList = new ArrayList<>();
                world.getNearbyEntities(location, 32, 32, 32, (e) -> e.getType() == org.bukkit.entity.EntityType.PLAYER)
                        .forEach(player -> {
                            if (player.getLocation().distance(location) > 31) return;
                            if (player.getName().startsWith("Loader-")) return; // Wild Loaders

                            newSeeingList.add(player.getUniqueId());

                            if (!seeingPlayers.contains(player.getUniqueId())) {
                                Bukkit.getScheduler().runTaskLaterAsynchronously(LitLibsPlugin.getInstance(), () -> spawn((Player) player), 5);
                            }
                        });

                seeingPlayers = new ArrayList<>(newSeeingList);
            }
        }.runTaskTimer(LitLibsPlugin.getInstance(), 0, 10);
    }

    public void spawn(Player player) {
        Location location = getLocation();
        com.github.retrooper.packetevents.protocol.world.Location spawnLocation = SpigotConversionUtil.fromBukkitLocation(location);

        WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(
                entityId,
                uuid,
                entityType,
                spawnLocation,
                location.getYaw(),
                0,
                null
        );

        queuePacket(spawnPacket, player);

        Bukkit.getScheduler().scheduleSyncDelayedTask(LitLibsPlugin.getInstance(), this::update, 5);
    }

    public void update() {
        updateRotation();
        updateEquipment(equipments);
    }

    public void updateEquipment(List<Equipment> equipments) {
        this.equipments = equipments;
        if (equipments == null || equipments.isEmpty()) return;

        WrapperPlayServerEntityEquipment equipmentPacket = new WrapperPlayServerEntityEquipment(
                entityId,
                equipments
        );

        queuePacketSeeing(equipmentPacket);
    }

    public void updateRotation() {
        WrapperPlayServerEntityRotation packet = new WrapperPlayServerEntityRotation(
                entityId,
                yaw,
                pitch,
                false
        );

        queuePacketSeeing(packet);
    }

    public void despawn() {
        updateSeeingTask.cancel();
        seeingPlayers.clear();

        Bukkit.getOnlinePlayers().forEach(player -> despawn(player, true));
    }

    public void despawn(Player player, boolean sync) {
        seeingPlayers.remove(player.getUniqueId());

        WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(
                entityId
        );

        if (sync) sendPacketSync(packet, player);
        else queuePacket(packet, player);
    }

    public void setGlowing(boolean glowing) {
        List<EntityData> entityDataList = new ArrayList<>();
        EntityData data2 = new EntityData(4, EntityDataTypes.BOOLEAN, glowing);
        entityDataList.add(data2);

        WrapperPlayServerEntityMetadata modifyPacket = new WrapperPlayServerEntityMetadata(
                entityId,
                entityDataList
        );

        queuePacketSeeing(modifyPacket);
    }

    public void setGlowingColor(ChatColor color) {
        setGlowing(true);

        Team team = npcHandler.getColorTeam(color);
        team.addEntry(uuid.toString());
    }

    public void queuePacketSeeing(PacketWrapper<?> packet) {
        Runnable runnable = () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                UUID playerUUID = player.getUniqueId();
                if (seeingPlayers.contains(playerUUID)) queuePacket(packet, player);
            });
        };

        npcHandler.getExecutor().execute(runnable);
    }

    public void queuePacket(PacketWrapper<?> packet) {
        Runnable runnable = () -> Bukkit.getOnlinePlayers().forEach(player -> sendPacket(packet, player));

        npcHandler.getExecutor().execute(runnable);
    }

    public void queuePacket(PacketWrapper<?> packet, Player player) {
        Runnable runnable = () -> sendPacket(packet, player);

        npcHandler.getExecutor().execute(runnable);
    }

    private void sendPacket(PacketWrapper<?> packet, Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(LitLibsPlugin.getInstance(),
                () -> sendPacketSync(packet, player));
    }

    private void sendPacketSync(PacketWrapper<?> packet, Player player) {
        try {
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
        }
        catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage("§b[LitLibs] §cFailed to send packet: " + packet.toString()
                    + " on player: " + player.getName() + " with uuid: " + player.getUniqueId());
        }
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }

    public void setLocation(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.worldName = location.getWorld().getName();
    }

}
