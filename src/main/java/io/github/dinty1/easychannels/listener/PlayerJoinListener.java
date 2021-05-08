package io.github.dinty1.easychannels.listener;

import io.github.dinty1.easychannels.EasyChannels;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("easychannels.adminalerts") && EasyChannels.getPlugin().getConfig().getBoolean("notify-admins-of-updates") && EasyChannels.getPlugin().isUpdateAvailable()) {
            event.getPlayer().sendMessage(ChatColor.BLUE + "An update for EasyChannels is available! Download it here: https://github.com/Dinty1/EasyChannels/releases");
        }
    }
}
