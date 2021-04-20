package io.github.dinty1.easychannels.manager;

import io.github.dinty1.easychannels.util.MessageUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        this.permission = channelInfo.get("permission") != null ? "easychannels." + channelInfo.get("permission").toString() : null;
        this.format = channelInfo.get("format").toString();
        this.discordFormat = channelInfo.get("discord-format") != null ? channelInfo.get("discord-format").toString() : null;
    }

    public void sendMessage(String message, Player author) {
        if (this.getPermission() == null) {
            Bukkit.broadcastMessage(format(message, author));
        } else {
            Bukkit.broadcast(format(message, author), this.getPermission());
        }
    }

    private String format(String message, Player author) {
        String output = MessageUtil.translateCodes(
                MessageUtil.replacePlaceholders(
                        this.getFormat(),
                        message,
                        author
                )
        );
        return output;
    }
}
