package com.spectrasonic.VoiceChatManager.managers;

import com.spectrasonic.VoiceChatManager.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class MessageManager {

    private final Main plugin;
    private final Map<String, String> messages = new HashMap<>();

    public MessageManager(Main plugin) {
        this.plugin = plugin;
    }

    public void loadMessages() {
        FileConfiguration config = plugin.getConfigManager().getConfig();
        messages.clear();

        if (config.contains("messages")) {
            for (String key : config.getConfigurationSection("messages").getKeys(false)) {
                messages.put(key, config.getString("messages." + key));
            }
        }
    }

    public String getMessage(String key) {
        return messages.getOrDefault(key, "");
    }
    
    public boolean hasMessage(String key) {
        return messages.containsKey(key);
    }
}
