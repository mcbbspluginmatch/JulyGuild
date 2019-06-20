package com.github.julyss2019.mcsp.julyguild;

import com.github.julyss2019.mcsp.julyguild.command.Command;
import com.github.julyss2019.mcsp.julyguild.command.MainGUICommand;
import com.github.julyss2019.mcsp.julyguild.config.Settings;
import com.github.julyss2019.mcsp.julyguild.guild.GuildManager;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julylibrary.command.JulyCommandExecutor;
import com.github.julyss2019.mcsp.julylibrary.config.JulyConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class JulyGuild extends JavaPlugin {
    private static JulyGuild instance;
    private GuildManager guildManager;
    private GuildPlayerManager guildPlayerManager;
    private Settings settings;
    private String[] initFolderPaths = new String[] {"players", "guilds"};
    private JulyCommandExecutor julyCommandExecutor;

    public void onEnable() {
        instance = this;

        init();
        loadConfig();

        this.guildManager = new GuildManager();
        this.guildPlayerManager = new GuildPlayerManager();
        this.julyCommandExecutor = new JulyCommandExecutor(this);

        getCommand("guild").setExecutor(julyCommandExecutor);
        registerCommands();
        getLogger().info("插件初始化完毕!");
    }

    private void registerCommands() {
        registerCommand(new MainGUICommand());
    }

    private void registerCommand(Command command) {
        julyCommandExecutor.register(command);
    }

    private void init() {
        for (String folderPath : initFolderPaths) {
            File folder = new File(getDataFolder(), folderPath);

            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    getLogger().severe("创建文件夹 " + folderPath + " 失败.");
                    setEnabled(false);
                    return;
                }
            }
        }
    }

    private void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
        settings = (Settings) JulyConfig.loadConfig(this, getConfig(), Settings.class);
    }

    public Settings getSettings() {
        return settings;
    }

    public GuildManager getGuildManager() {
        return guildManager;
    }

    public GuildPlayerManager getGuildPlayerManager() {
        return guildPlayerManager;
    }

    public static JulyGuild getInstance() {
        return instance;
    }
}
