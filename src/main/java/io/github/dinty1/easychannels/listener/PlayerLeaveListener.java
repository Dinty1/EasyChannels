package io.github.dinty1.easychannels.listener;

import io.github.dinty1.easychannels.EasyChannels;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        EasyChannels.getChannelManager().removeAutoChannel(event.getPlayer());
    }
}
