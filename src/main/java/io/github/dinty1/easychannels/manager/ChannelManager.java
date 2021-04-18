package io.github.dinty1.easychannels.manager;

import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.command.ChannelCommand;
import io.github.dinty1.easychannels.util.CommandUtil;
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
            EasyChannels.info("Loaded channel " + channel.getName());
            // Register commands
            try {
                CommandUtil.registerCommand(new ChannelCommand(channel, channel.getCommands()), EasyChannels.getPlugin(EasyChannels.class));
                EasyChannels.info("Registered channel command " + channel.getCommands().get(0));
            } catch (ReflectiveOperationException e) {
                EasyChannels.error("An error occured while attempting to register the channel command " + channel.getCommands().get(0), e);
            }
        }
    }

    public @Nullable Channel getChannel(@NotNull String name) {
        return channels.get(name);
    }
}
