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
import io.github.dinty1.easychannels.manager.ChannelManager;
import io.github.dinty1.easychannels.object.Channel;
import io.github.dinty1.easychannels.util.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LeaveCommand extends AbstractChannelOrientedCommand {

    @Override
    public void execute(Channel channel, Player player) {
        if (!channel.getNotListening().contains(player.getUniqueId())) channel.getNotListening().add(player.getUniqueId());
        if (!ConfigUtil.Message.CHANNEL_LEFT.isBlank())
            player.sendMessage(ConfigUtil.Message.CHANNEL_LEFT.replaceChannelPlaceholder(channel));
        ChannelManager manager = EasyChannels.getChannelManager();
        // If the player is autochatting here then remove them from that
        if (manager.getAutoChannel(player) != null && manager.getAutoChannel(player).getName().equals(channel.getName()))
            manager.removeAutoChannel(player);
    }
}
