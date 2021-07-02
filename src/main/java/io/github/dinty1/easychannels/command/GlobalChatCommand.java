package io.github.dinty1.easychannels.command;

import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.object.Channel;
import io.github.dinty1.easychannels.manager.ChannelManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GlobalChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        ChannelManager manager = EasyChannels.getChannelManager();
        if (args.length < 1) { // If command doesn't have a message attached
            manager.removeAutoChannel(player);
            sender.sendMessage(ChatColor.BLUE + "Channel set: " + ChatColor.GRAY + "global");
        } else { // Player wants to send a one-off message to the global channel
            Channel autoChannel = manager.getAutoChannel(player); // Save their current autoChannel
            manager.removeAutoChannel(player); // Briefly clear so that we can send a message
            player.chat(String.join(" ", args));
            manager.setAutoChannel(player, autoChannel);
        }
        return true;
    }
}
