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

package io.github.dinty1.easychannels.manager;

import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.command.ChannelCommand;
import io.github.dinty1.easychannels.object.Channel;
import io.github.dinty1.easychannels.object.InvalidChannelException;
import io.github.dinty1.easychannels.util.CommandUtil;
import io.github.dinty1.easychannels.util.MessageUtil;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class ChannelManager {

    public @Getter Map<String, Channel> channels = new HashMap<>();
    public @Getter Map<String, String> channelCommands = new HashMap<>();
    @Getter(AccessLevel.PRIVATE) private HashMap<String, String> playerAutoChannels = new HashMap<>();

    public void registerChannelsAndCommands(@NotNull List<Map<?, ?>> channelConfig) {
        // Register channels
        for (final Map i : channelConfig) {
            try {
                Channel channel = new Channel(i);
                channels.put(i.get("name").toString(), channel);
                EasyChannels.info("Loaded channel " + channel.getName());
                // Register commands
                CommandUtil.registerCommand(new ChannelCommand(channel), EasyChannels.getPlugin(EasyChannels.class));

            } catch (InvalidChannelException e) {
                EasyChannels.error(e.getMessage());
            } catch (ReflectiveOperationException e) {
                EasyChannels.error("An error occured while attempting to register a channel command.", e);
            }

        }
    }

    public @Nullable Channel getChannel(@NotNull String name) {
        return channels.get(name);
    }

    public void setAutoChannel(Player player, Channel channel) {
        if (channel == null) return; // Ignore GlobalChatCommand trying to set the player back to the global channel
        this.getPlayerAutoChannels().put(player.getName(), channel.getName());
    }

    public @Nullable Channel getAutoChannel(Player player) {
        return this.getChannel(this.getPlayerAutoChannels().get(player.getName()));
    }

    public void removeAutoChannel(Player player) {
        this.getPlayerAutoChannels().remove(player.getName());
    }

    public String getGlobalChannelFormat(@NotNull String message, @NotNull Player author) {
        return MessageUtil.translateCodes(
                MessageUtil.replacePlaceholders(
                        Objects.requireNonNull(EasyChannels.getPlugin().getConfig().getString("global-format")),
                        message,
                        author
                )
        );

    }

    // TODO rename to getChannelsAsList() and deprecate this
    public List<String> getChannelList() {
        return new ArrayList<>(this.getChannels().keySet());
    }
}
