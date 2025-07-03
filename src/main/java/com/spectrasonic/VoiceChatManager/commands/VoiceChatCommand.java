package com.spectrasonic.VoiceChatManager.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.spectrasonic.VoiceChatManager.Main;
import com.spectrasonic.Utils.MessageUtils;
import com.spectrasonic.Utils.SoundUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@CommandAlias("vc")
public class VoiceChatCommand extends BaseCommand {

    private final Main plugin;
    private final LuckPerms luckPerms;
    private final String PERMISSION_NODE = "voicechat.speak";
    private final String TARGET_GROUP = "default";
    

    public VoiceChatCommand(Main plugin) {
        this.plugin = plugin;
        this.luckPerms = plugin.getLuckPermsApi();
    }

    @Subcommand("mute")
    @CommandPermission("voicechat.manage")
    @CommandCompletion("all|*")
    @Syntax("<target>")
    @Description("Mutea el permiso de voz para el grupo por defecto. Usa 'all' o '*' como objetivo.")
    public void onMute(Player sender, String target) {
        handleVoiceChatAction(sender, "mute", target);
    }

    @Subcommand("unmute")
    @CommandPermission("voicechat.manage")
    @CommandCompletion("all|*")
    @Syntax("<target>")
    @Description("Desmutea el permiso de voz para el grupo por defecto. Usa 'all' o '*' como objetivo.")
    public void onUnmute(Player sender, String target) {
        handleVoiceChatAction(sender, "unmute", target);
    }

    private void handleVoiceChatAction(Player sender, String action, String target) {
        target = target.toLowerCase();

        if (!target.equals("all") && !target.equals("*")) {
            MessageUtils.sendMessage(sender, "<red>Uso incorrecto. El target debe ser 'all' o '*'.");
            return;
        }

        boolean setPermission = action.equals("unmute");
        MessageUtils.sendConsoleMessage("Ejecutando comando " + action + " para grupo default");

        CompletableFuture<Group> groupFuture = CompletableFuture.supplyAsync(() -> luckPerms.getGroupManager().getGroup(TARGET_GROUP));

        groupFuture.thenAcceptAsync(group -> {
            if (group == null) {
                MessageUtils.sendMessage(sender, "<red>El grupo default no existe.");
                return;
            }
            // Clear old permission node from group
            group.data().clear(node -> NodeType.PERMISSION.matches(node) && NodeType.PERMISSION.cast(node).getPermission().equals(PERMISSION_NODE));
            
            // Add new permission node with desired value
            Node nodeToUpdate = Node.builder(PERMISSION_NODE).value(setPermission).build();
            group.data().add(nodeToUpdate);

            luckPerms.getGroupManager().saveGroup(group);

            // Notify command sender group operation complete
            String groupMessageKey = setPermission ? "unmute-group-message" : "mute-group-message";
            MessageUtils.sendMessage(sender, plugin.getMessageManager().getMessage(groupMessageKey));

            // Send to players in group default
            Bukkit.getOnlinePlayers().stream().filter(p -> isInDefaultGroup(p)).forEach(p -> {
                String playerMsgKey = setPermission ? "unmute-player-message" : "mute-player-message";
                MessageUtils.sendMessage(p, plugin.getMessageManager().getMessage(playerMsgKey));
                SoundUtils.playerSound(p, org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
            });
        });
    }

    private boolean isInDefaultGroup(Player player) {
        User user = luckPerms.getPlayerAdapter(org.bukkit.entity.Player.class).getUser(player);
        if (user == null) return false; // Defensive

        return user.getInheritedGroups(user.getQueryOptions()).stream()
                .anyMatch(g -> g.getName().equalsIgnoreCase(TARGET_GROUP));
    }

    // Comando para recargar config y mensajes
    @Subcommand("reload")
    @CommandPermission("voicechat.manage")
    public void onReload(Player player) {
        plugin.getConfigManager().loadConfig();
        plugin.getMessageManager().loadMessages();
        MessageUtils.sendMessage(player, "<green>Configuración Recargada.");
    }
@Subcommand("help")
    @Description("Muestra la ayuda para los comandos de VoiceChat.")
    public void onHelp(Player sender) {
        MessageUtils.sendMessage(sender, "<green>Comandos disponibles:");
        MessageUtils.sendMessage(sender, "<green>/vc mute <all|*> - Mutea el permiso de voz para el grupo por defecto.");
        MessageUtils.sendMessage(sender, "<green>/vc unmute <all|*> - Desmutea el permiso de voz para el grupo por defecto.");
        MessageUtils.sendMessage(sender, "<green>/vc reload - Recarga la configuración y los mensajes.");
    }
    @Default
    @Description("Muestra la ayuda para los comandos de VoiceChat.")
    public void onDefault(Player sender) {
        MessageUtils.sendMessage(sender, "<green>Comandos disponibles:");
        MessageUtils.sendMessage(sender, "<green>/vc mute <all|*> - Mutea el permiso de voz para el grupo por defecto.");
        MessageUtils.sendMessage(sender, "<green>/vc unmute <all|*> - Desmutea el permiso de voz para el grupo por defecto.");
        MessageUtils.sendMessage(sender, "<green>/vc reload - Recarga la configuración y los mensajes.");
    }
}
