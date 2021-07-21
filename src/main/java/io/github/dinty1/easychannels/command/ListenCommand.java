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
