package io.github.dinty1.easychannels;

import io.github.dinty1.easychannels.managers.ChannelManager;
import io.github.dinty1.easychannels.managers.ConfigManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public class EasyChannels extends JavaPlugin {
    @Getter private ConfigManager configManager;
    @Getter @Setter(AccessLevel.PRIVATE) private ChannelManager channelManager;

    @Override
    public void onEnable() {
        // First thing's first, save the config
        this.saveDefaultConfig();

        // Initialise the config manager
        this.configManager = new ConfigManager(this);
    }
}
