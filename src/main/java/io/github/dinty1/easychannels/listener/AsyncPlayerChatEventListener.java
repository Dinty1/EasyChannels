package io.github.dinty1.easychannels.listener;

import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.manager.Channel;
import io.github.dinty1.easychannels.manager.ChannelManager;
import io.github.dinty1.easychannels.util.MessageUtil;
import org.bukkit.Bukkit;
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
            Bukkit.getScheduler().runTask(EasyChannels.getPlugin(), () -> { // Handle this on a tick
                playerChannel.sendMessage(event.getMessage(), event.getPlayer());
            });
        } else if (EasyChannels.getPlugin().getConfig().getBoolean("modify-global-chat")){
            event.setFormat(EasyChannels.getChannelManager().getGlobalChannelFormat(event.getMessage(), event.getPlayer()));
        }
    }
}
