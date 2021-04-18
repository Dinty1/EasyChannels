package io.github.dinty1.easychannels;

import io.github.dinty1.easychannels.managers.ChannelManager;
import io.github.dinty1.easychannels.util.ConfigUtil;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class EasyChannels extends JavaPlugin {
    @Getter private static ChannelManager channelManager = new ChannelManager();

    @Override
    public void onEnable() {
        // First thing's first, save the config
        this.saveDefaultConfig();

        // Register stuff
        getChannelManager().registerChannelsAndCommands();
    }

    @Override
    public void onDisable() {

    }

    public static void info(String message) {
        EasyChannels.getPlugin(EasyChannels.class).getLogger().info(message);
    }
}
