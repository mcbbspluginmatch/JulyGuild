package com.github.julyss2019.mcsp.julyguild;

import com.github.julyss2019.mcsp.julyguild.command.Command;
import com.github.julyss2019.mcsp.julyguild.command.MainGUICommand;
import com.github.julyss2019.mcsp.julyguild.command.ReloadCommand;
import com.github.julyss2019.mcsp.julyguild.config.ConfigGuildIcon;
import com.github.julyss2019.mcsp.julyguild.config.GuildSettings;
import com.github.julyss2019.mcsp.julyguild.config.IconShopSettings;
import com.github.julyss2019.mcsp.julyguild.guild.CacheGuildManager;
import com.github.julyss2019.mcsp.julyguild.guild.GuildManager;
import com.github.julyss2019.mcsp.julyguild.listener.GUIListener;
import com.github.julyss2019.mcsp.julyguild.listener.TpAllListener;
import com.github.julyss2019.mcsp.julyguild.log.GuildLog;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julyguild.tasks.RequestCleanTask;
import com.github.julyss2019.mcsp.julyguild.util.Util;
import com.github.julyss2019.mcsp.julylibrary.command.JulyCommandExecutor;
import com.github.julyss2019.mcsp.julylibrary.config.JulyConfig;
import com.github.julyss2019.mcsp.julylibrary.logger.FileLogger;
import com.github.julyss2019.mcsp.julylibrary.logger.JulyFileLogger;
import com.google.gson.Gson;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class JulyGuild extends JavaPlugin {
    public static final String CONFIG_VERSION = "1.3.9";
    private static final Gson gson = new Gson();
    private static JulyGuild instance;
    private GuildManager guildManager;
    private GuildPlayerManager guildPlayerManager;
    private CacheGuildManager cacheGuildManager;
    private GuildSettings guildSettings;
    private IconShopSettings iconShopSettings;
    private String[] initFolderPaths = new String[] {"players", "guilds", "logs"};
    private String[] initFilePaths = new String[] {"config.yml", "icon_shop.yml", "guild_shop.yml"};
    private JulyCommandExecutor julyCommandExecutor;
    private PlayerPointsAPI playerPointsAPI;
    private Economy vaultAPI;
    private FileLogger fileLogger;
    private PlaceholderAPIExpansion placeholderAPIExpansion;

    @Override
    public void onEnable() {
        instance = this;

        init();
        loadConfig();

        if (!selfCheck()) {
            return;
        }

        this.playerPointsAPI = ((PlayerPoints) Bukkit.getPluginManager().getPlugin("PlayerPoints")).getAPI();
        this.fileLogger = JulyFileLogger.getLogger(new File(getDataFolder(), "logs"), null, 5);
        this.julyCommandExecutor = new JulyCommandExecutor(this);
        this.guildPlayerManager = new GuildPlayerManager();
        this.guildManager = new GuildManager();
        this.cacheGuildManager = new CacheGuildManager();
        this.placeholderAPIExpansion = new PlaceholderAPIExpansion();

        if (!placeholderAPIExpansion.register()) {
            getLogger().warning("PlaceholderAPI Hook 失败!");
        }

        julyCommandExecutor.setPrefix("§a[宗门] §f");
        guildManager.loadGuilds();
        cacheGuildManager.startTask();

        Util.sendColoredMessage(Bukkit.getConsoleSender(), "载入了 " + guildManager.getGuilds().size() + "个 宗门.");
        getCommand("jguild").setExecutor(julyCommandExecutor);
        registerCommands();
        registerListeners();
        runTasks();
        Util.sendColoredMessage(Bukkit.getConsoleSender(), "插件初始化完毕, 作者QQ: 884633197, 接MC插件定制.");
    }

    @Override
    public void onDisable() {
        if (PlaceholderAPI.isRegistered("guild")) {
            PlaceholderAPI.unregisterExpansion(placeholderAPIExpansion);
        }

        Util.sendColoredMessage(Bukkit.getConsoleSender(), "插件被卸载, 作者QQ: 884633197, 接MC插件定制.");
    }

    private void runTasks() {
        new RequestCleanTask().runTaskTimerAsynchronously(this, 0L, 20L);
    }

    public CacheGuildManager getCacheGuildManager() {
        return cacheGuildManager;
    }

    public IconShopSettings getIconShopSettings() {
        return iconShopSettings;
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
        Bukkit.getPluginManager().registerEvents(new TpAllListener(), this);
    }

    public void writeGuildLog(FileLogger.LoggerLevel loggerLevel, GuildLog log) {
        fileLogger.log(loggerLevel, gson.toJson(log));
    }

    public FileLogger getFileLogger() {
        return fileLogger;
    }

    private boolean selfCheck() {
        if (!setupEconomy()) {
            getLogger().severe("Vault Hook 失败!");
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
        registerCommand(new ReloadCommand());
    }

    private void registerCommand(Command command) {
        julyCommandExecutor.register(command);
    }

    private void init() {
        for (String folderPath : initFolderPaths) {
            File folder = new File(getDataFolder(), folderPath);

            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    setEnabled(false);
                    throw new RuntimeException("&c创建文件夹失败: " + folderPath);
                }
            }
        }

        for (String filePath : initFilePaths) {
            File file = new File(getDataFolder(), filePath);

            if (!file.exists()) {
                saveResource(filePath, false);
            }
        }
    }

    public void reloadPluginConfig() {
        guildSettings.reset();
        iconShopSettings.reset();

        JulyConfig.reloadConfig(this, YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml")), guildSettings);
        JulyConfig.reloadConfig(this, YamlConfiguration.loadConfiguration(new File(getDataFolder(), "icon_shop.yml")), iconShopSettings);

        loadSpecialConfig();
    }

    private void loadConfig() {
        reloadConfig(); // 必须先重读，否则将发生死循环
        String oldConfigVersion = getConfig().getString("config_version", "0.0.0");

        // 如果配置版本不一样，重命名文件，创建新的配置
        if (!CONFIG_VERSION.equals(oldConfigVersion)) {
            File configFile = new File(getDataFolder(), "config.yml");

            if (configFile.exists() && !configFile.renameTo(new File(getDataFolder(), "config.yml." + oldConfigVersion))) {
                throw new RuntimeException("&c配置文件更新失败.");
            }

            getLogger().warning("配置文件版本不一致, 将重新生成(" + oldConfigVersion + "->" + CONFIG_VERSION + ")");
            saveDefaultConfig();
            loadConfig();
            return;
        }

        this.guildSettings = (GuildSettings) JulyConfig.loadConfig(this, YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml")), GuildSettings.class);
        this.iconShopSettings = (IconShopSettings) JulyConfig.loadConfig(this, YamlConfiguration.loadConfiguration(new File(getDataFolder(), "icon_shop.yml")), IconShopSettings.class);

        loadSpecialConfig();
    }

    private void loadSpecialConfig() {
        FileConfiguration iconShopConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "icon_shop.yml"));

        if (iconShopConfig.contains("items")) {
            ConfigurationSection iconItemSection = iconShopConfig.getConfigurationSection("items");

            for (String key : iconItemSection.getKeys(false)) {
                ConfigurationSection itemSection = iconItemSection.getConfigurationSection(key);
                ConfigGuildIcon configGuildIcon = new ConfigGuildIcon();

                configGuildIcon.setMaterial(Material.valueOf(itemSection.getString("material")));
                configGuildIcon.setDurability((short) itemSection.getInt("durability"));
                configGuildIcon.setDisplayName(itemSection.getString("display_name"));
                configGuildIcon.setLores(itemSection.getStringList("lores"));
                configGuildIcon.setMoneyPayEnabled(itemSection.getBoolean("cost.money.enabled"));
                configGuildIcon.setPointsPayEnabled(itemSection.getBoolean("cost.points.enabled"));
                configGuildIcon.setMoneyCost(itemSection.getInt("cost.money.amount"));
                configGuildIcon.setPointsCost(itemSection.getInt("cost.points.amount"));

                iconShopSettings.addConfigGuildIcon(configGuildIcon);
            }
        }
    }

    public GuildSettings getGuildSettings() {
        return guildSettings;
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
