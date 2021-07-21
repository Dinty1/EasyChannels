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
