package io.github.dinty1.easychannels.command;

import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.manager.ChannelManager;
import io.github.dinty1.easychannels.object.Channel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LeaveCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // TODO command to listen
        if (args.length < 1) return false;
        Channel channel = EasyChannels.getChannelManager().getChannel(args[0]);
        if (channel == null) return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }
        Player player = (Player) sender;

        if (channel.getPermission() != null && !player.hasPermission(channel.getPermission())) {
            player.sendMessage(ChatColor.RED + "You do not have access to that channel.");
        } else {
            if (!channel.getNotListening().contains(player.getUniqueId())) channel.getNotListening().add(player.getUniqueId());
            sender.sendMessage(ChatColor.BLUE + "Channel left: " + ChatColor.GRAY + channel.getName());
            ChannelManager manager = EasyChannels.getChannelManager();
            // If the player is autochatting here then remove them from that
            if (manager.getAutoChannel(player) != null && manager.getAutoChannel(player).getName().equals(channel.getName()))
                manager.removeAutoChannel(player);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return EasyChannels.getChannelManager().getChannelList();
    }
}
