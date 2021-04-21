package io.github.dinty1.easychannels.command;

import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.object.Channel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChannelCommand extends BukkitCommand {
    private Channel channel;

    public ChannelCommand(Channel channel, List<String> commands) {
        super(commands.get(0));// Register command with the first alias
        this.usageMessage = "/" + commands.get(0) + " [message]";
        this.description = "Send a message to/toggle automatic chatting in the " + channel.getName() + " channel.";
        commands.remove(0);// Remove the already registered command so that we can add everything else as aliases
        this.setAliases(commands);
        if (this.getPermission() != null) {
            this.setPermission(this.getPermission());
        }
        this.channel = channel; // Save channel object for later use
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use chat channels.");
        } else if (this.channel.getPermission() != null && !sender.hasPermission(this.channel.getPermission())) {
            sender.sendMessage(ChatColor.RED + "You do not have access to this channel.");
        } else if (args.length == 0) {
            EasyChannels.getChannelManager().setAutoChannel((Player) sender, this.channel);
            sender.sendMessage(ChatColor.BLUE + "You are now automatically chatting in the \"" + this.channel.getName() + "\" channel.");
        } else {
            channel.sendMessage(String.join(" ", args), (Player) sender);
        }
        return true;
    }
}
