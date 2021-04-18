package io.github.dinty1.easychannels.util;

import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

public class CommandUtil {
    // Black magic that I copied off some forum
    public static void registerCommand(Command command, Plugin plugin) throws ReflectiveOperationException {
        Method commandMap = plugin.getServer().getClass().getMethod("getCommandMap", null);
        Object cmdMap = commandMap.invoke(plugin.getServer(), null);
        Method register = cmdMap.getClass().getMethod("register", String.class, Command.class);
        register.invoke(cmdMap, plugin.getName(), command);
    }
}
