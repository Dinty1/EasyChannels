package io.github.dinty1.easychannels.object;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.emoji.EmojiParser;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.util.MessageUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Channel {
    @Getter private String name;
    @Getter private List<String> commands;
    @Getter @Nullable private String permission;
    @Getter private String format;
    @Getter @Nullable private String discordFormat;
    private boolean isInvalid = true;

    public Channel(Map channelInfo) throws InvalidChannelException {
        this.name = channelInfo.get("name").toString();
        this.commands = (List<String>) channelInfo.get("commands");
        this.permission = channelInfo.get("permission") != null ? "easychannels." + channelInfo.get("permission").toString() : null;
        this.format = channelInfo.get("format").toString();
        this.discordFormat = channelInfo.get("discord-format") != null ? channelInfo.get("discord-format").toString() : null;
        if (this.name == null || this.commands == null || this.format == null) {
            throw new InvalidChannelException("One of the required channel options is null.");
        } else if (this.name.equals("global")) {
            throw new InvalidChannelException("Custom channels cannot be named \"global\"");
        }
    }

    public void sendMessage(String message, Player author) {
        if (this.getPermission() == null) {
            Bukkit.broadcastMessage(format(message, author));
        } else {
            Bukkit.broadcast(format(message, author), this.getPermission());
        }

        if (EasyChannels.discordSrvHookEnabled()) {
            Bukkit.getScheduler().runTaskAsynchronously(EasyChannels.getPlugin(), () -> {
                DiscordSRV.getPlugin().processChatMessage(author, message, this.getName(), false);
            });
        }
    }

    public void sendMessageFromDiscord(@NotNull Message message) {
        String text = EmojiParser.parseToAliases(message.getContentStripped());
        String format = this.getDiscordFormat();
        if (format == null || format.equals("")) return; // No format set so go no further
        text = MessageUtil.replaceDiscordPlaceholders(format, text, Objects.requireNonNull(message.getMember()));
        text = MessageUtil.translateCodes(text);
        if (this.getPermission() == null) {
            Bukkit.broadcastMessage(text);
        } else {
            Bukkit.broadcast(text, this.getPermission());
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
