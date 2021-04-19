package io.github.dinty1.easychannels.util;

import io.github.dinty1.easychannels.EasyChannels;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtil {
    public static String translateCodes(String message) {
        String output = message;
        output = ChatColor.translateAlternateColorCodes('&', output);
        return output;
    }

    public static String replacePlaceholders(String format, String message, Player player) {
        return format
                .replace("%username%", player.getName())
                .replace("%displayname%", player.getDisplayName())
                .replace("%prefix%", EasyChannels.getChat().getPlayerPrefix(player))
                .replace("%suffix%", EasyChannels.getChat().getPlayerSuffix(player))
                .replace("%message%", message);
    }
}
