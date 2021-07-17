package io.github.dinty1.easychannels.object;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.emoji.EmojiParser;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class Channel {
    @Getter private final String name;
    @Getter private final List<String> commands;
    @Getter @Nullable private final String permission;
    @Getter private final String format;
    @Getter @Nullable private final String discordFormat;
    @Getter @Setter private List<UUID> notListening = new ArrayList<>();
    @Getter @Setter int range;

    @SuppressWarnings("unchecked")
    public Channel(Map<String, ?> channelInfo) throws InvalidChannelException {
        this.name = channelInfo.get("name").toString();
        this.commands = (List<String>) channelInfo.get("commands");
        this.permission = channelInfo.get("permission") != null ? "easychannels." + channelInfo.get("permission").toString() : null;
        this.format = channelInfo.get("format").toString();
        this.discordFormat = channelInfo.get("discord-format") != null ? channelInfo.get("discord-format").toString() : null;
        this.range = channelInfo.get("range") == null || Integer.parseInt(channelInfo.get("range").toString()) < 1 ? 0 : Integer.parseInt(channelInfo.get("range").toString());
        if (this.name == null || this.commands == null || this.format == null || this.commands.size() < 1) {
            throw new InvalidChannelException("One of the required channel options is null (or empty).");
        } else if (this.name.equals("global")) {
            throw new InvalidChannelException("Custom channels cannot be named \"global\"");
        }
    }

    public void sendMessage(String message, Player author) {
        // Player is sending a message so they evidently want to see what's happening in this channel
        this.notListening.remove(author.getUniqueId());
        if (this.getPermission() == null) {
            for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (this.getRange() != 0 && author.getWorld() != p.getWorld()) continue; // If it's ranged and they're in a different world, skip
                if (!this.notListening.contains(p.getUniqueId()) && (this.getRange() == 0 || author.getLocation().distance(p.getLocation()) <= this.getRange())) {
                    p.sendMessage(this.format(message, author));
                }
            }
            Bukkit.getConsoleSender().sendMessage(this.format(message, author)); // Broadcast message to console
        } else {
            for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
                assert this.permission != null;
                if (this.getRange() != 0 && author.getWorld() != p.getWorld()) continue; // If it's ranged and they're in a different world, skip
                if (!this.notListening.contains(p.getUniqueId()) && p.hasPermission(this.permission) && (this.getRange() == 0 || author.getLocation().distance(p.getLocation()) <= this.getRange())) {
                    p.sendMessage(this.format(message, author));
                }
            }
            Bukkit.getConsoleSender().sendMessage(this.format(message, author));
        }

        if (EasyChannels.discordSrvHookEnabled()) {
            Bukkit.getScheduler().runTaskAsynchronously(EasyChannels.getPlugin(), () -> {
                DiscordSRV.getPlugin().processChatMessage(author, message, this.getName(), false);
            });
        }
    }
    // TODO optimize method above, test, handle attachments
    public void sendMessageFromDiscord(@NotNull Message message) {
        final List<String> messages = new ArrayList<>();
        messages.add(EmojiParser.parseToAliases(message.getContentStripped()));
        messages.addAll(message.getAttachments().stream().map(Message.Attachment::getUrl).collect(Collectors.toList()));
        for (String text : messages) {
            String format = this.getDiscordFormat();
            if (format == null || format.equals("")) return; // No format set so go no further
            text = MessageUtil.replaceDiscordPlaceholders(format, text, Objects.requireNonNull(message.getMember()));
            text = MessageUtil.translateCodes(text);
            for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (this.getPermission() != null) {
                    if (!p.hasPermission(this.getPermission())) continue;
                }
                if (!this.notListening.contains(p.getUniqueId())) {
                    p.sendMessage(text);
                }
            }
            Bukkit.getConsoleSender().sendMessage(text); // Broadcast message to console
        }
    }

    private String format(String message, Player author) {
        return MessageUtil.translateCodes(
                MessageUtil.replacePlaceholders(
                        this.getFormat(),
                        message,
                        author
                )
        );
    }
}
