package io.github.dinty1.easychannels.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MiscUtil {

    @Nullable public static <K, V> K getKeyByValue(@NotNull Map<K, V> map, @NotNull V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
