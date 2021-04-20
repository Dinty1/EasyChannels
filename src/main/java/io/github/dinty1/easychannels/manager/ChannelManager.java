package io.github.dinty1.easychannels.manager;

import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.command.ChannelCommand;
import io.github.dinty1.easychannels.util.CommandUtil;
import io.github.dinty1.easychannels.util.MessageUtil;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ChannelManager {

    public @Getter Map<String, Channel> channels = new HashMap<>();
    public @Getter Map<String, String> channelCommands = new HashMap<>();
    @Getter(AccessLevel.PRIVATE) private HashMap<String, String> playerAutoChannels = new HashMap<>();

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
}
