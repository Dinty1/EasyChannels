/*-
 * LICENSE
 * EasyChannels
 * -------------
 * Copyright (C) 2021 Dinty1
 * -------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END
 */

package io.github.dinty1.easychannels.util;

import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

public class CommandUtil {
    // Black magic that I copied off some forum
    public static void registerCommand(Command command, Plugin plugin) throws ReflectiveOperationException {
        Method commandMap = plugin.getServer().getClass().getMethod("getCommandMap");
        Object cmdMap = commandMap.invoke(plugin.getServer());
        Method register = cmdMap.getClass().getMethod("register", String.class, Command.class);
        register.invoke(cmdMap, plugin.getName(), command);
    }
}
