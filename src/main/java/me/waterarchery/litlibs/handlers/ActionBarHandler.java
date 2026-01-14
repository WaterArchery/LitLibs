package me.waterarchery.litlibs.handlers;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.utils.ChatUtils;
import me.waterarchery.litlibs.version.Version;
import me.waterarchery.litlibs.version.VersionHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class ActionBarHandler {

    private final LitLibs litLibs;

    public ActionBarHandler(LitLibs litLibs) {
        this.litLibs = litLibs;
    }

    public void sendActionBar(Player player, String message) {
        if (player == null || message == null) {
            return;
        }

        String coloredMessage = ChatUtils.colorizeLegacy(message);
        VersionHandler versionHandler = VersionHandler.getInstance();

        if (versionHandler.isServerNewerThan(Version.v1_11)) {
            try {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(coloredMessage));
            } catch (Exception ex) {
                sendLegacyActionBar(player, coloredMessage);
            }
        } else {
            sendLegacyActionBar(player, coloredMessage);
        }
    }

    private void sendLegacyActionBar(Player player, String message) {
        try {
            Object chatComponent = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                    .getMethod("a", String.class)
                    .invoke(null, "{\"text\": \"" + message + "\"}");

            Constructor<?> packetConstructor = getNMSClass("PacketPlayOutChat")
                    .getConstructor(getNMSClass("IChatBaseComponent"), byte.class);

            Object packet = packetConstructor.newInstance(chatComponent, (byte) 2);
            sendPacket(player, packet);
        } catch (Exception ex) {
            litLibs.getLogger().error(ex.getMessage());
        }
    }

    private void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception ex) {
            litLibs.getLogger().error(ex.getMessage());
        }
    }

    private Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server" + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException ex) {
            litLibs.getLogger().error(ex.getMessage());
        }
        return null;
    }

}

