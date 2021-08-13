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

import io.github.dinty1.easychannels.object.Channel;
import io.github.dinty1.easychannels.util.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ListenCommand extends AbstractChannelOrientedCommand {

    @Override
    public void execute(Channel channel, Player player) {
        if (channel.getNotListening().contains(player.getUniqueId())) {
            channel.getNotListening().remove(player.getUniqueId());
            if (!ConfigUtil.Message.NOW_LISTENING.isBlank())
                player.sendMessage(ConfigUtil.Message.NOW_LISTENING.replaceChannelPlaceholder(channel));
        } else player.sendMessage(ChatColor.RED + "You are already listening to that channel!");
    }
}
