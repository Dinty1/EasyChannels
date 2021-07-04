package io.github.dinty1.easychannels.command;

import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.object.Channel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChannelListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }
        Map<String, Channel> channels = EasyChannels.getChannelManager().getChannels();
        sender.sendMessage(ChatColor.BLUE + "You have access to the following channels:");
        List<Channel> allowedChannels = new ArrayList<>();
        for (Channel c : channels.values()) {
            if (c.getPermission() == null) {
                allowedChannels.add(c);
            } else if (sender.hasPermission(c.getPermission())) {
                allowedChannels.add(c);
            }
        }
        Player player = (Player) sender;

        for (Channel c : allowedChannels) {
            sender.sendMessage(ChatColor.GREEN + c.getName() + " - /" + c.getCommands().get(0) + (c.getPermission() == null ? "" : ChatColor.DARK_AQUA + " [Permission required]") + (c.getNotListening().contains(player.getUniqueId()) ? ChatColor.RED + " [Not Listening]" : "") + (c.getRange() != 0 ? ChatColor.DARK_PURPLE + " [Ranged]" : ""));
        }
        return true;
    }
}
