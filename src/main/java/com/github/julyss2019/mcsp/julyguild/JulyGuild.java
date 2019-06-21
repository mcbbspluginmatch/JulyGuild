package com.github.julyss2019.mcsp.julyguild;

import com.github.julyss2019.mcsp.julyguild.command.Command;
import com.github.julyss2019.mcsp.julyguild.command.CreateCommand;
import com.github.julyss2019.mcsp.julyguild.command.MainGUICommand;
import com.github.julyss2019.mcsp.julyguild.config.Settings;
import com.github.julyss2019.mcsp.julyguild.guild.GuildManager;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julylibrary.command.JulyCommandExecutor;
import com.github.julyss2019.mcsp.julylibrary.config.JulyConfig;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class JulyGuild extends JavaPlugin {
    public static final String CONFIG_VERSION = "1.0.0";
    private static JulyGuild instance;
    private GuildManager guildManager;
    private GuildPlayerManager guildPlayerManager;
    private Settings settings;
    private String[] initFolderPaths = new String[] {"players", "guilds"};
    private JulyCommandExecutor julyCommandExecutor;
    private PlayerPointsAPI playerPointsAPI;
    private Economy vaultAPI;

    public void onEnable() {
        instance = this;

        init();
        loadConfig();

        if (!selfCheck()) {
            return;
        }

        this.playerPointsAPI = ((PlayerPoints) Bukkit.getPluginManager().getPlugin("PlayerPoints")).getAPI();
        this.guildManager = new GuildManager();
        this.guildPlayerManager = new GuildPlayerManager();
        this.julyCommandExecutor = new JulyCommandExecutor(this);

        getCommand("guild").setExecutor(julyCommandExecutor);
        registerCommands();
        JulyMessage.setPrefix(this, "§a[宗门] ");
        getLogger().info("插件初始化完毕!");
    }

    private boolean selfCheck() {
        if (!setupEconomy()) {
            getLogger().severe("Vault Hook 失败!");
            setEnabled(false);
            return false;
        }

        if (settings.isCreateGuildCostPointsEnabled() && !Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
            getLogger().severe("PlayerPoints Hook 失败!");
            setEnabled(false);
            return false;
        }

        return true;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

        if (economyProvider != null) {
            vaultAPI = economyProvider.getProvider();
        }

        return (vaultAPI != null);
    }

    private void registerCommands() {
        registerCommand(new MainGUICommand());
        registerCommand(new CreateCommand());
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
        reloadConfig(); // 必须先重读，否则将发生死循环
        String oldConfigVersion = getConfig().getString("config_version", "0.0.0");

        // 如果配置版本不一样，重命名文件，创建新的配置
        if (!CONFIG_VERSION.equals(oldConfigVersion)) {
            new File(getDataFolder(), "config.yml").renameTo(new File(getDataFolder(), "config.yml." + oldConfigVersion));
            getLogger().warning("配置文件版本不一致, 将重新生成(" + oldConfigVersion + "->" + CONFIG_VERSION + ")");
            saveDefaultConfig();
            loadConfig();
            return;
        }

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

    public PlayerPointsAPI getPlayerPointsAPI() {
        return playerPointsAPI;
    }

    public Economy getVaultAPI() {
        return vaultAPI;
    }
}
