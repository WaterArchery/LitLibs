package me.waterarchery.litlibs.utils;

import me.waterarchery.litlibs.LitLibsPlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

public class ChatUtils {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(StandardTags.color())
                    .resolver(StandardTags.gradient())
                    .resolver(StandardTags.reset())
                    .resolver(StandardTags.rainbow())
                    .resolver(StandardTags.decorations())
                    .build())
            .postProcessor(component -> component.decoration(TextDecoration.ITALIC, false))
            .build();

    private static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.legacySection();

    public static void sendPlayerMessage(Player player, TextComponent component) {
        LitLibsPlugin litLibsPlugin = LitLibsPlugin.getInstance();
        BukkitAudiences adventure = litLibsPlugin.adventure();

        adventure.player(player).sendMessage(component);
    }

    public static void broadcastMessage(TextComponent component) {
        LitLibsPlugin litLibsPlugin = LitLibsPlugin.getInstance();
        BukkitAudiences adventure = litLibsPlugin.adventure();

        adventure.all().sendMessage(component);
    }

    public static void sendSenderMessage(CommandSender sender, TextComponent component) {
        LitLibsPlugin litLibsPlugin = LitLibsPlugin.getInstance();
        BukkitAudiences adventure = litLibsPlugin.adventure();

        adventure.sender(sender).sendMessage(component);
    }

    public static void sendPlayerMessage(Player player, String message) {
        LitLibsPlugin litLibsPlugin = LitLibsPlugin.getInstance();
        BukkitAudiences adventure = litLibsPlugin.adventure();

        TextComponent component = ChatUtils.colorize(message);
        adventure.player(player).sendMessage(component);
    }

    public static void sendSenderMessage(CommandSender sender, String message) {
        LitLibsPlugin litLibsPlugin = LitLibsPlugin.getInstance();
        BukkitAudiences adventure = litLibsPlugin.adventure();

        TextComponent component = ChatUtils.colorize(message);
        adventure.sender(sender).sendMessage(component);
    }

    public static TextComponent colorize(String message) {
        if (message == null) return Component.text("null text");

        message = updateColors(message);
        return (TextComponent) MINI_MESSAGE.deserialize(
                LEGACY_COMPONENT_SERIALIZER.serialize(Component.text(message))
        );
    }

    public static String colorizeLegacy(String message) {
        if (message == null) return "null text";
        message = updateColors(message);

        try {
            return LEGACY_COMPONENT_SERIALIZER.serialize(
                    MINI_MESSAGE.deserialize(message)
            );
        }
        catch (Exception ignored) {
            return LEGACY_COMPONENT_SERIALIZER.serialize(
                    LEGACY_COMPONENT_SERIALIZER.deserialize(message)
            );
        }
    }

    private static String updateColors(String str) {
        if (str.contains("&#")) {
            final Pattern hexPattern = Pattern.compile("&#" + "([A-Fa-f0-9]{6})");
            Matcher matcher = hexPattern.matcher(str);
            StringBuilder buffer = new StringBuilder(str.length() + 4 * 8);
            while (matcher.find())
            {
                String group = matcher.group(1);
                matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                        + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                        + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                        + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
                );
            }
            str = matcher.appendTail(buffer).toString();
        }

        return str.replace("&", "§");
    }

}
