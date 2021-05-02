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
