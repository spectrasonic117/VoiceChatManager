package com.spectrasonic.VoiceChatManager;

import com.spectrasonic.VoiceChatManager.managers.CommandManager;
import com.spectrasonic.VoiceChatManager.managers.ConfigManager;
import com.spectrasonic.VoiceChatManager.managers.MessageManager;
import org.bukkit.plugin.java.JavaPlugin;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class Main extends JavaPlugin {

    private CommandManager commandManager;
    private ConfigManager configManager;
    private MessageManager messageManager;
    private LuckPerms luckPermsApi;

    @Override
    public void onEnable() {
        if (!setupLuckPerms()) {
            getLogger().severe("LuckPerms not found! Disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);

        this.commandManager = new CommandManager(this);
        this.commandManager.registerCommands();

        configManager.loadConfig();
        messageManager.loadMessages();

        // Setup default group permissions
        if (luckPermsApi != null) {
            new com.spectrasonic.VoiceChatManager.permissions.PermissionSetupManager(this, luckPermsApi).setupDefaultGroupPermissions();
        } else {
            getLogger().warning("LuckPerms API not available, skipping default group permission setup.");
        }

        // Set Plugin in utils
        com.spectrasonic.Utils.CommandUtils.setPlugin(this);

        // Send startup message to console
        com.spectrasonic.Utils.MessageUtils.sendStartupMessage(this);
    }

    @Override
    public void onDisable() {
        com.spectrasonic.Utils.MessageUtils.sendShutdownMessage(this);
    }

    private boolean setupLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPermsApi = provider.getProvider();
            return true;
        }
        return false;
    }

    public LuckPerms getLuckPermsApi() {
        return luckPermsApi;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}
