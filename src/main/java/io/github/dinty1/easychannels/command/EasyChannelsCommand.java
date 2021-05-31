package io.github.dinty1.easychannels.command;

import io.github.dinty1.easychannels.EasyChannels;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EasyChannelsCommand implements CommandExecutor, TabExecutor {
    private final String[] subcommands = new String[]{"reloadconfig"};
    private final List<String> subcommandsAsList = new ArrayList<String>(Arrays.asList(subcommands));
    private EasyChannels plugin;

    public EasyChannelsCommand(EasyChannels plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1) return false;
        switch (args[0]) {
            case "reloadconfig":
                if (!sender.hasPermission("easychannels.reloadconfig")) {
                    sender.sendMessage(ChatColor.RED + "You do not have access to this command.");
                    return true;
                }
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.BLUE + "The EasyChannels configuration has been reloaded. Keep in mind that changing anything channel-specific requires a server restart.");
                break;
            default:
                return false;
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 2) return subcommandsAsList;
        else return null;
    }
}
