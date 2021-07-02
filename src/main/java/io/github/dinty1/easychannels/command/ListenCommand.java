package io.github.dinty1.easychannels.command;

import io.github.dinty1.easychannels.object.Channel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ListenCommand extends AbstractChannelOrientedCommand {

    @Override
    public void execute(Channel channel, Player player) {
        if (channel.getNotListening().contains(player.getUniqueId())) {
            channel.getNotListening().remove(player.getUniqueId());
            player.sendMessage(ChatColor.BLUE + "Now listening to " + ChatColor.GRAY + channel.getName());
        } else player.sendMessage(ChatColor.RED + "You are already listening to that channel!");
    }
}
