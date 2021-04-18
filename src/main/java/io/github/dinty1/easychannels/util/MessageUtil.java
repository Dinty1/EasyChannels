package io.github.dinty1.easychannels.util;

import org.bukkit.ChatColor;

public class MessageUtil {
    public static String translateCodes(String message) {
        String output = message;
        output = ChatColor.translateAlternateColorCodes('&', output);
        return output;
    }
}
