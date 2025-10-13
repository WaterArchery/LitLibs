package me.waterarchery.litlibs.handlers;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.utils.ChatUtils;
import me.waterarchery.litlibs.version.Version;
import me.waterarchery.litlibs.version.VersionHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class TitleHandler {

    private final LitLibs litLibs;

    public TitleHandler(LitLibs litLibs) {
        this.litLibs = litLibs;
    }

    public void sendTitle(Player player, String rawTitle, String rawSubTitle) {
        sendTitle(player, rawTitle, rawSubTitle, 5, 30, 5);
    }

    public void sendTitle(Player player, String rawTitle, String rawSubTitle, int duration) {
        sendTitle(player, rawTitle, rawSubTitle, 5, duration, 5);
    }

    public void sendTitle(Player player, String rawTitle, String rawSubTitle, int fadeIn, int duration, int fadeOut) {
        String title = ChatUtils.colorizeLegacy(rawTitle);
        String subTitle = ChatUtils.colorizeLegacy(rawSubTitle);
        VersionHandler versionHandler = VersionHandler.getInstance();

        if (versionHandler.isServerNewerThan(Version.v1_10)) player.sendTitle(title, subTitle, fadeIn, duration, fadeOut);
        else sendLegacyTitle(player, title, subTitle, fadeIn, duration, fadeOut);
    }

    private void sendLegacyTitle(Player player, String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        try {
            Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
                    .invoke(null, "{\"text\": \"" + title + "\n" + subtitle + "\"}");

            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE")
                    .get(null), chatTitle, fadeInTime, showTime, fadeOutTime);

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


    /**
     * Get NMS class using reflection
     *
     * @param name Name of the class
     * @return Class
     */
    private Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server" + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException ex) {
            litLibs.getLogger().error(ex.getMessage());
        }
        return null;
    }

}
