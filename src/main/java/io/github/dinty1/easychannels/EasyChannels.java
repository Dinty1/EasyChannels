package io.github.dinty1.easychannels;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.dinty1.easychannels.command.GlobalChatCommand;
import io.github.dinty1.easychannels.listener.AsyncPlayerChatEventListener;
import io.github.dinty1.easychannels.listener.DiscordGuildMessagePreProcessListener;
import io.github.dinty1.easychannels.listener.PluginEnableListener;
import io.github.dinty1.easychannels.manager.ChannelManager;
import io.github.dinty1.easychannels.util.ConfigUtil;
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class EasyChannels extends JavaPlugin {
    @Getter private static ChannelManager channelManager = new ChannelManager();
    @Getter private static Chat chat;
    private static boolean discordSrvHook = false;

    @Override
    public void onEnable() {
        // First thing's first, save the config
        this.saveDefaultConfig();

        // Register stuff
        getChannelManager().registerChannelsAndCommands(ConfigUtil.getChannels(this));

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatEventListener(), this);
        getServer().getPluginManager().registerEvents(new PluginEnableListener(), this);

        getCommand("globalchat").setExecutor(new GlobalChatCommand());

        // Set up vault thing
        setupChat();
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
}
