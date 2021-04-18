package io.github.dinty1.easychannels.util;

import io.github.dinty1.easychannels.EasyChannels;

import java.util.List;
import java.util.Map;

public class ConfigUtil {

    public static List<Map<?,?>> getChannels(EasyChannels plugin) {
        return plugin.getConfig().getMapList("channels");
    }
}
