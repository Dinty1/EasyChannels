package io.github.dinty1.easychannels.managers;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class ChannelManager {
    @Getter private Map channels;
    @Getter(AccessLevel.PRIVATE) private HashMap<Player, String> playerAutoChannels = new HashMap<>();

    public ChannelManager(@NotNull Object channels) {

    }
}
