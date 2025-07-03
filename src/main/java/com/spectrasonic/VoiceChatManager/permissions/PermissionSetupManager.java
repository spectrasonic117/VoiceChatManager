package com.spectrasonic.VoiceChatManager.permissions;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import com.spectrasonic.VoiceChatManager.Main;

import java.util.concurrent.CompletableFuture;
import java.util.Optional;

public class PermissionSetupManager {

    private final Main plugin;
    private final LuckPerms luckPermsApi;

    public PermissionSetupManager(Main plugin, LuckPerms luckPermsApi) {
        this.plugin = plugin;
        this.luckPermsApi = luckPermsApi;
    }

    public void setupDefaultGroupPermissions() {
        CompletableFuture<Optional<Group>> defaultGroupFuture = luckPermsApi.getGroupManager().loadGroup("default");

        defaultGroupFuture.thenAcceptAsync(optionalGroup -> {
            if (!optionalGroup.isPresent()) {
                plugin.getLogger().warning("Default group not found in LuckPerms. Cannot set default permissions.");
                return;
            }
            Group defaultGroup = optionalGroup.get();

            boolean listenPermissionExists = defaultGroup.getNodes().stream()
                    .filter(NodeType.PERMISSION::matches)
                    .map(NodeType.PERMISSION::cast)
                    .anyMatch(node -> node.getPermission().equals("voicechat.listen") && node.getValue());

            boolean speakPermissionExists = defaultGroup.getNodes().stream()
                    .filter(NodeType.PERMISSION::matches)
                    .map(NodeType.PERMISSION::cast)
                    .anyMatch(node -> node.getPermission().equals("voicechat.speak") && node.getValue());

            boolean changed = false;

            if (!listenPermissionExists) {
                Node listenNode = PermissionNode.builder("voicechat.listen").value(true).build();
                defaultGroup.data().add(listenNode);
                plugin.getLogger().info("Added voicechat.listen permission to default group.");
                changed = true;
            }

            if (!speakPermissionExists) {
                Node speakNode = PermissionNode.builder("voicechat.speak").value(true).build();
                defaultGroup.data().add(speakNode);
                plugin.getLogger().info("Added voicechat.speak permission to default group.");
                changed = true;
            }

            if (changed) {
                luckPermsApi.getGroupManager().saveGroup(defaultGroup);
                plugin.getLogger().info("Default group permissions updated successfully.");
            } else {
                plugin.getLogger().info("Default group already has voicechat.listen and voicechat.speak permissions.");
            }
        }).exceptionally(e -> {
            plugin.getLogger().severe("Error loading default group: " + e.getMessage());
            return null;
        });
    }
}