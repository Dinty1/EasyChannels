package io.github.dinty1.easychannels.command;

import io.github.dinty1.easychannels.manager.Channel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChannelCommand extends BukkitCommand {

    public ChannelCommand(Channel channel, List<String> commands) {
        super(commands.get(0));// Register command with the first alias
        this.usageMessage = "/" + commands.get(0) + " [message]";
        this.description = "Send a message to/toggle automatic chatting in the " + channel.getName() + " channel.";
        commands.remove(0);// Remove the already registered command so that we can add everything else as aliases
        this.setAliases(commands);
        if (channel.getPermission() != null) {
            this.setPermission(channel.getPermission());
            this.setPermissionMessage(ChatColor.RED + "You do not have permission to access this channel.");
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        sender.sendMessage("hi");
        return true;
    }
}
