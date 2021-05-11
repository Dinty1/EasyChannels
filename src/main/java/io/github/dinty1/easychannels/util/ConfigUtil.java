package io.github.dinty1.easychannels.util;

import io.github.dinty1.easychannels.EasyChannels;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ConfigUtil {

    public static List<Map<?, ?>> getChannels(EasyChannels plugin) {
        return plugin.getConfig().getMapList("channels");
    }

    public static void migrate(FileConfiguration oldConfig, EasyChannels easyChannels) throws IOException {
        if (!oldConfig.getString("config-version").equals(easyChannels.getDescription().getVersion())) {
            easyChannels.getLogger().info("Your config version does not match the plugin version, migrating...");

            // Load config
            File oldConfigFile = new File(easyChannels.getDataFolder(), "config.yml");

            List<String> channelConfigLines = new ArrayList<>(); // We want to keep the channel configuration intact while migrating
            boolean readingChannelConfig = false;

            // Put values in map
            Scanner oldConfigReader = new Scanner(oldConfigFile);
            Map<String, String> oldConfigMap = new HashMap<>();
            while (oldConfigReader.hasNextLine()) {
                final String line = oldConfigReader.nextLine();
                if (line.startsWith("#")) continue;
                if (readingChannelConfig) {
                    channelConfigLines.add(line);
                    continue;
                }
                final String[] split = line.split(":", 2);
                if (split[0].equals("channels")) {
                    readingChannelConfig = true;
                    continue;
                }
                if (split.length != 2) continue;
                oldConfigMap.put(split[0], split[1].trim());

            }
            oldConfigReader.close();
            oldConfigFile.delete();

            readingChannelConfig = false; // Will be re-used later

            // Load new config
            easyChannels.saveDefaultConfig();

            File newConfigFile = new File(easyChannels.getDataFolder(), "config.yml");

            // Change values where necessary
            Scanner newConfigReader = new Scanner(newConfigFile);
            final List<String> newConfigLines = new ArrayList<>();
            while (newConfigReader.hasNextLine()) {
                final String line = newConfigReader.nextLine();
                if (readingChannelConfig && !line.startsWith("#")) continue; // Channel comments can stay
                newConfigLines.add(line);
                if (line.startsWith("config-version") || line.startsWith("#")) continue;
                final String[] split = line.split(":", 2);
                if (split[0].equals("channels")) {
                    readingChannelConfig = true; // Re-use this variable
                    continue;
                }
                if (split.length != 2) continue;
                if (oldConfigMap.containsKey(split[0])) {
                    split[1] = oldConfigMap.get(split[0]);
                    newConfigLines.set(newConfigLines.size() - 1, String.join(": ", split));
                    easyChannels.getLogger().info("Migrated config option " + split[0] + " with value " + split[1]);
                }
            }

            // Add channel configuration to the new config
            newConfigLines.addAll(channelConfigLines);

            final String newConfig = String.join(System.lineSeparator(), newConfigLines);
            // Doing this the hard way for cross-platform compatibility (cough linux)
            FileWriter fileWriter = new FileWriter(new File(easyChannels.getDataFolder(), "config.yml"));
            fileWriter.write(newConfig);
            fileWriter.close();
        }
    }
}
