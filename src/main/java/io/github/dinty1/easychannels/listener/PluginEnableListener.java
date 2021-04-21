package io.github.dinty1.easychannels.listener;

import io.github.dinty1.easychannels.EasyChannels;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginEnableListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    @SuppressWarnings("unused")
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin().getName().equals("DiscordSRV")) {
            EasyChannels.info("DiscordSRV detected, hooking into DiscordSRV");
            EasyChannels.setDiscordSrvHookEnabled(true);
        }
    }
}
