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

public class ChatUtils {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(StandardTags.defaults())
                    .build())
            .strict(false)
            .emitVirtuals(false)
            .postProcessor(component -> component.decoration(TextDecoration.ITALIC, false))
            .build();

    private static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
            .character('§')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

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

        message = message.replace("&", "§");
        return (TextComponent) MINI_MESSAGE.deserialize(
                LEGACY_COMPONENT_SERIALIZER.serialize(Component.text(message))
        );
    }

    public static String colorizeLegacy(String message) {
        if (message == null) return "null text";

        message = message.replace("&", "§");
        try {
            Component component = MINI_MESSAGE.deserialize(message);
            return LEGACY_COMPONENT_SERIALIZER.serialize(component);
        }
        catch (Exception ignored) {
            ignored.printStackTrace();
            return LEGACY_COMPONENT_SERIALIZER.serialize(
                    LEGACY_COMPONENT_SERIALIZER.deserialize(message));
        }
    }

}
