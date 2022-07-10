package com.vanquil.prison.tools.util.container.config;

import java.util.List;
import java.util.Map;

public abstract class ContainerConfig {
    protected List<String> format;
    protected char closeCharacter = 'X';
    protected Map<String, ContainerItemConfig> itemMap;

    public boolean verifyFormat() {
        if (format.size() > 6 || format.size() < 3)
            return false;
        for (String line : format) {
            if (line.length() != 9)
                return false;
        }
        return true;
    }

    public List<String> format() {
        return format;
    }

    public Map<String, ContainerItemConfig> itemMap() {
        return itemMap;
    }

    public char closeCharacter() {
        return closeCharacter;
    }
}
