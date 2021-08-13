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

package io.github.dinty1.easychannels.listener;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePreProcessEvent;
import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.object.Channel;
import io.github.dinty1.easychannels.util.MiscUtil;
import org.bukkit.Bukkit;

import java.util.Map;

public class DiscordGuildMessagePreProcessListener {

    @Subscribe
    @SuppressWarnings("unused")
    public void onDiscordGuildMessagePreProcess(DiscordGuildMessagePreProcessEvent event) {

        Map<String, String> linkedChannels = DiscordSRV.getPlugin().getChannels();
        if (linkedChannels.containsValue(event.getChannel().getId())) { // If this channel is linked to somewhere
            String linkedChannelName = MiscUtil.getKeyByValue(linkedChannels, event.getChannel().getId());
            if (linkedChannelName == null) return; // Couldn't resolve the name so do nothing
            Channel linkedChannel = EasyChannels.getChannelManager().getChannel(linkedChannelName);
            if (linkedChannel == null) return; // Couldn't resolve the channel
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(EasyChannels.getPlugin(), () -> { // Send message on a tick
                linkedChannel.sendMessageFromDiscord(event.getMessage());
            });
        }
    }
}
