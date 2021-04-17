package io.github.dinty1.easychannels.managers;

import io.github.dinty1.easychannels.EasyChannels;

public class ConfigManager {
    private EasyChannels plugin;

    public ConfigManager(EasyChannels plugin) {
        this.plugin = plugin;
    }

    public Object getChannels() {
        return this.plugin.getConfig().get("channels", this.getClass());
    }
}
