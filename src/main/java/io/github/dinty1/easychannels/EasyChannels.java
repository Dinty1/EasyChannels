package io.github.dinty1.easychannels;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.dinty1.easychannels.command.ChannelListCommand;
import io.github.dinty1.easychannels.command.GlobalChatCommand;
import io.github.dinty1.easychannels.command.LeaveCommand;
import io.github.dinty1.easychannels.listener.*;
import io.github.dinty1.easychannels.manager.ChannelManager;
import io.github.dinty1.easychannels.object.UpdateChecker;
import io.github.dinty1.easychannels.util.ConfigUtil;
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class EasyChannels extends JavaPlugin {
    @Getter private static ChannelManager channelManager = new ChannelManager();
    @Getter private static Chat chat;
    private static boolean discordSrvHook = false;
    private boolean updateAvailable = false;

    @Override
    public void onEnable() {
        // First thing's first, save the config
        this.saveDefaultConfig();

        // Migrate config if needed
        try {
            ConfigUtil.migrate(this.getConfig(), this);
        } catch (IOException e) {
            error("An error occurred while attempting to migrate the configuration.", e);
        }

        // Register stuff
        getChannelManager().registerChannelsAndCommands(ConfigUtil.getChannels(this));

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new PluginEnableListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        getCommand("globalchat").setExecutor(new GlobalChatCommand());
        getCommand("channels").setExecutor(new ChannelListCommand());
        getCommand("leavechannel").setExecutor(new LeaveCommand());

        // Set up vault thing
        setupChat();

        // Metrics and shit
        Metrics metrics = new Metrics(this, 11106);
        metrics.addCustomChart(new SimplePie("number_of_channels", () -> String.valueOf(getChannelManager().getChannels().size())));

        // Check for updates
        getLogger().info("Checking for updates...");
        new UpdateChecker(this, 91958).getLatestVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getLogger().info("Up to date!");
            } else {
                getLogger().info("An update is available! Get it here: https://github.com/Dinty1/EasyChannels/releases");
                this.updateAvailable = true;
            }
        });

        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {

    }

    public static void info(String message) {
        EasyChannels.getPlugin(EasyChannels.class).getLogger().info(message);
    }

    public static void error(String message, @Nullable Exception e) {
        EasyChannels.getPlugin(EasyChannels.class).getLogger().severe(message);
        if (e != null) {
            e.printStackTrace();
        }
    }

    public static void error(String message) {
        error(message, null);
    }

    public static EasyChannels getPlugin() {
        return getPlugin(EasyChannels.class);
    }

    public void setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
    }

    public static boolean discordSrvHookEnabled() {
        return discordSrvHook;
    }

    public static void setDiscordSrvHookEnabled(boolean enabled) {
        discordSrvHook = enabled;
        if (enabled) {
            DiscordSRV.api.subscribe(new DiscordGuildMessagePreProcessListener());
        }
    }

    public boolean isUpdateAvailable() {
        return this.updateAvailable;
    }
}
