package com.spectrasonic.VoiceChatManager.managers;

import com.spectrasonic.VoiceChatManager.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final Main plugin;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }
}
