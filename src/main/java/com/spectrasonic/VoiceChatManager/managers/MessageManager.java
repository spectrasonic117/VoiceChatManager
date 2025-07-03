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

        messages.put("mute-group-message", config.getString("messages.mute-group-message", "<red>Jugadores Sienciados"));
        messages.put("unmute-group-message", config.getString("messages.unmute-group-message", "<green>Jugadores con Permiso de Habla"));
        messages.put("mute-player-message", config.getString("messages.mute-player-message", "<red>Microfono desactivado, Escucha atentamente las instrucciones "));
        messages.put("unmute-player-message", config.getString("messages.unmute-player-message", "<green>Ahora tienes permiso de hablar nuevamente, diviertete"));
    }

    public String getMessage(String key) {
        return messages.getOrDefault(key, "");
    }
}
