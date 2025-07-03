package com.spectrasonic.VoiceChatManager.managers;

import co.aikar.commands.PaperCommandManager;
import com.spectrasonic.VoiceChatManager.Main;
import com.spectrasonic.VoiceChatManager.commands.VoiceChatCommand;

public class CommandManager {
    private final Main plugin;
    private PaperCommandManager acf;

    public CommandManager(Main plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        this.acf = new PaperCommandManager(plugin);
        // Register the commands class
        this.acf.registerCommand(new VoiceChatCommand(plugin));
    }
}
