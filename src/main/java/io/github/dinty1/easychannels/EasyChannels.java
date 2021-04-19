package io.github.dinty1.easychannels;

import io.github.dinty1.easychannels.listener.AsyncPlayerChatEventListener;
import io.github.dinty1.easychannels.manager.ChannelManager;
import io.github.dinty1.easychannels.util.ConfigUtil;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class EasyChannels extends JavaPlugin {
    @Getter private static ChannelManager channelManager = new ChannelManager();

    @Override
    public void onEnable() {
        // First thing's first, save the config
        this.saveDefaultConfig();

        // Register stuff
        getChannelManager().registerChannelsAndCommands(ConfigUtil.getChannels(this));

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatEventListener(), this);
    }

    @Override
    public void onDisable() {

    }

    public static void info(String message) {
        EasyChannels.getPlugin(EasyChannels.class).getLogger().info(message);
    }

    public static void error(String message, @Nullable Exception e) {
        EasyChannels.getPlugin(EasyChannels.class).getLogger().severe(message);
        if(e != null) {
            e.printStackTrace();
        }
    }
}
