package io.github.dinty1.easychannels.managers;

import io.github.dinty1.easychannels.EasyChannels;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChannelManager {

    public @Getter Map<String, Channel> channels = new HashMap<>();
    public @Getter Map<String, String> channelCommands = new HashMap<>();
    @Getter(AccessLevel.PRIVATE) private HashMap<Player, String> playerAutoChannels = new HashMap<>();

    public void registerChannelsAndCommands(@NotNull List<Map<?, ?>> channelConfig) {
        // Register channels
        for (final Map i : channelConfig) {
            // TODO channel validation
            Channel channel = new Channel(i);
            channels.put(i.get("name").toString(), channel);
            EasyChannels.info("Loaded channel " + channel.getDiscordFormat());
            // Register commands
        }
    }

    public @Nullable Channel getChannel(@NotNull String name) {
        return channels.get(name);
    }
}
