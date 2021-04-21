package io.github.dinty1.easychannels.listener;

import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.object.Channel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatEventListener implements Listener {

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
            event.setFormat(EasyChannels.getChannelManager().getGlobalChannelFormat(event.getMessage(), event.getPlayer()));
        }
    }
}
