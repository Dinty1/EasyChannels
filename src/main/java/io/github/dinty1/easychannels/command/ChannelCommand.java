/*-
 * LICENSE
 * EasyChannels
 * -------------
 * Copyright (C) 2021 Dinty1
 * -------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END
 */

package io.github.dinty1.easychannels.command;

import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.object.Channel;
import io.github.dinty1.easychannels.util.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChannelCommand extends BukkitCommand {
    private Channel channel;

    public ChannelCommand(Channel channel) {
        super(channel.getCommands().get(0));// Register command with the first alias
        final List<String> commands = channel.getCommands();
        this.usageMessage = "/" + commands.get(0) + " [message]";
        this.description = "Send a message to/toggle automatic chatting in the " + channel.getName() + " channel.";
        this.setAliases(commands.subList(1, commands.size()));// Remove the already registered command so that we can add everything else as aliases
        if (this.getPermission() != null) {
            this.setPermission(this.getPermission());
        }
        this.channel = channel;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use chat channels.");
        } else if (this.channel.getPermission() != null && !sender.hasPermission(this.channel.getPermission())) {
            sender.sendMessage(ChatColor.RED + "You do not have access to this channel.");
        } else if (args.length == 0) {
            EasyChannels.getChannelManager().setAutoChannel((Player) sender, this.channel);
            // Remove player from non-listen list
            channel.getNotListening().remove(((Player) sender).getUniqueId());
            if (!ConfigUtil.Message.CHANNEL_SET.isBlank())
                sender.sendMessage(ConfigUtil.Message.CHANNEL_SET.replaceChannelPlaceholder(this.channel));
        } else {
            channel.sendMessage(String.join(" ", args), (Player) sender);
        }
        return true;
    }
}
