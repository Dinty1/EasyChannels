package io.github.dinty1.easychannels.util;

import io.github.dinty1.easychannels.EasyChannels;
import io.github.dinty1.easychannels.object.Channel;
import org.bukkit.ChatColor;
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
            easyChannels.getLogger().info("Your config version does not match the plugin version, updating...");
            easyChannels.getLogger().info("You should check your config after to make sure it's alright");

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

    public static Set<String> findMissingChannelOptions(Map<String, ?> channelInfo) {
        final Set<String> missingChannelOptions = new HashSet<>();
        final Set<String> optionsToCheck = new HashSet<>(Arrays.asList("name", "commands", "format"));
        for (final String option : optionsToCheck) {
            if (channelInfo.get(option) == null || channelInfo.get(option).equals("")) missingChannelOptions.add(option);
        }
        return missingChannelOptions;
    }

    public enum Message {
        CHANNEL_SET("channel-set-message"),
        CHANNEL_LEFT("channel-left-message"),
        NOW_LISTENING("now-listening-message");

        private final String configOption;

        Message(String configOption) {
            this.configOption = configOption;
        }

        @Override
        public String toString() {
            final String message = Objects.requireNonNull(EasyChannels.getPlugin().getConfig().getString(this.configOption));
            return ChatColor.translateAlternateColorCodes('&', message);
        }

        public String replaceChannelPlaceholder(Channel channel) {
            return this.toString().replace("%channel%", channel.getName());
        }

        public String replaceChannelPlaceholder(String replacement) {
            return this.toString().replace("%channel%", replacement);
        }

        public boolean isBlank() {
            return this.toString().equals("");
        }
    }
}
