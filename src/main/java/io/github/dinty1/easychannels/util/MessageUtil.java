/*-
 * LICENSE
 * EasyChannels
 * -------------
 * Copyright (C) 2021 Dinty1
 * -------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END
 */

package io.github.dinty1.easychannels.util;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import io.github.dinty1.easychannels.EasyChannels;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

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
                .replace("%prefix%", EasyChannels.getChat() == null ? "" : EasyChannels.getChat().getPlayerPrefix(player))
                .replace("%suffix%", EasyChannels.getChat() == null ? "" : EasyChannels.getChat().getPlayerSuffix(player))
                .replace("%message%", message);
    }

    public static String replaceDiscordPlaceholders(String format, String message, Member member) {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            format = PlaceholderAPI.setPlaceholders(null, format);
        }
        format = format
                .replace("%username%", member.getUser().getName())
                .replace("%name%", member.getNickname() != null ? member.getNickname() : member.getUser().getName())
                .replace("%tag%", member.getUser().getAsTag());

        // Copied from https://github.com/DiscordSRV/DiscordSRV/blob/master/src/main/java/github/scarsz/discordsrv/listeners/DiscordChatListener.java
        // strip colors if role doesn't have permission
        List<String> rolesAllowedToColor = DiscordSRV.config().getStringList("DiscordChatChannelRolesAllowedToUseColorCodesInChat");
        boolean shouldStripColors = !rolesAllowedToColor.contains("@everyone");
        for (Role role : member.getRoles())
            if (rolesAllowedToColor.contains(role.getName())) shouldStripColors = false;
        if (shouldStripColors) message = github.scarsz.discordsrv.util.MessageUtil.stripLegacy(message);

        format = format.replace("%message%", message);
        format = translateCodes(format);
        return format;
    }
}
