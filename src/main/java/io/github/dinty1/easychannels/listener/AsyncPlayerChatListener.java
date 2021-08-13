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

package io.github.dinty1.easychannels.listener;

import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.object.Channel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Channel playerChannel = EasyChannels.getChannelManager().getAutoChannel(event.getPlayer());
        if (playerChannel != null) { // If the player is auto-chatting in a specific channel
            event.setCancelled(true);
            if (playerChannel.getPermission() != null && !event.getPlayer().hasPermission(playerChannel.getPermission())) { // Player doesn't have permission to chat here any more
                event.getPlayer().sendMessage(ChatColor.RED + "You no longer have permission to access this channel. Returning you to the global channel...");
                EasyChannels.getChannelManager().removeAutoChannel(event.getPlayer());
                return;
            }
            Bukkit.getScheduler().runTask(EasyChannels.getPlugin(), () -> { // Handle this on a tick
                playerChannel.sendMessage(event.getMessage(), event.getPlayer());
            });
        } else if (EasyChannels.getPlugin().getConfig().getBoolean("modify-global-chat")){
            event.setFormat(EasyChannels.getChannelManager().getGlobalChannelFormat(event.getMessage(), event.getPlayer()).replace("%", "%%"));
        }
    }
}
