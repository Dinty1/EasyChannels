package io.github.dinty1.easychannels.managers;

import lombok.Getter;

import java.util.List;
import java.util.Map;

public class Channel {
    @Getter private String name;
    @Getter private List<String> commands;
    @Getter private String permission;
    @Getter private String format;
    @Getter private String discordFormat;

    public Channel(Map channelInfo) {
        this.name = channelInfo.get("name").toString();
        this.commands = (List<String>) channelInfo.get("commands");
        this.permission = channelInfo.get("permission") != null ? channelInfo.get("permission").toString() : null;
        this.format = channelInfo.get("format").toString();
        this.discordFormat = channelInfo.get("discord-format") != null ? channelInfo.get("discord-format").toString() : null;
    }

    public void sendMessage() {

    }
}
