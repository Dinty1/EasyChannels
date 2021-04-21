package io.github.dinty1.easychannels.util;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import io.github.dinty1.easychannels.EasyChannels;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtil {
    public static String translateCodes(String message) {
        String output = message;
        output = ChatColor.translateAlternateColorCodes('&', output);
        return output;
    }

    public static String replacePlaceholders(String format, String message, Player player) {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            format = PlaceholderAPI.setPlaceholders(player, format);
        }
        return format
                .replace("%username%", player.getName())
                .replace("%displayname%", player.getDisplayName())
                .replace("%prefix%", EasyChannels.getChat().getPlayerPrefix(player))
                .replace("%suffix%", EasyChannels.getChat().getPlayerSuffix(player))
                .replace("%message%", message);
    }

    public static String replaceDiscordPlaceholders(String format, String message, Member member) {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            format = PlaceholderAPI.setPlaceholders(null, format);
        }
        return format
                .replace("%username%", member.getUser().getName())
                .replace("%name%", member.getNickname() != null ? member.getNickname() : member.getUser().getName())
                .replace("%tag%", member.getUser().getAsTag())
                .replace("%message%", message);
    }
}
