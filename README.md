# VoiceChatManager

A Minecraft plugin for managing voice chat functionality on Paper Servers. Requires Java 21, depends of [LuckPerms](https://luckperms.net/download) and [SimpleVoiceChat](https://modrinth.com/plugin/simple-voice-chat/versions).

## Requirements

-   Minecraft 1.21+
-   Java 21
-   Paper Server (latest recommended)
-   [LuckPerms](https://luckperms.net/download)
-   [Simple Voice Chat](https://modrinth.com/plugin/simple-voice-chat/versions)

## Installation

1. Download the latest release of `VoiceChatManager`.
2. Place the compiled `.jar` file into the `plugins` folder of your Paper Server.
3. Restart or reload the server.

## Compilation

1. Ensure Minecraft 1.21+ is installed.
2. Navigate to the project directory and run:
    ```bash
    mvn clean install
    ```
3. The compiled plugin will be available in the `target` directory.

## Usage

### Commands

-   `/voicechat mute all` - Mute yourself in the channel.
-   `/voicechat unmute all` - Unmute yourself in the channel.

### Permissions

-   `voicechat.admin` - Allows joining a voice chat channel.

## Configuration

Edit `config.yml` to customize the plugin settings. The file will be generated upon first run.
