package io.github.dinty1.easychannels.managers;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Channel {
    @Getter public String name;
    @Getter public String[] commands;
    @Getter public String permission;
    @Getter public String format;
    @Getter public String discordFormat;

    public Channel(@NotNull String name, @NotNull String[] commands, @Nullable String permission, @NotNull String format, @Nullable String discordFormat) {

    }
}
