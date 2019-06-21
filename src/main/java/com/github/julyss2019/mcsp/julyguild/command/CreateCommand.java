package com.github.julyss2019.mcsp.julyguild.command;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.Settings;
import com.github.julyss2019.mcsp.julyguild.guild.GuildManager;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julylibrary.message.JulyMessage;
import com.github.julyss2019.mcsp.julylibrary.message.TitleBuilder;
import com.github.julyss2019.mcsp.julylibrary.utils.ItemUtil;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CreateCommand implements Command {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildManager guildManager = plugin.getGuildManager();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();
    private static Settings settings = plugin.getSettings();
    private static Economy vault = plugin.getVaultAPI();
    private static PlayerPointsAPI playerPointsAPI = plugin.getPlayerPointsAPI();

    @Override
    public boolean onCommand(CommandSender cs, String[] args) {
        if (args.length == 2) {
            String guildName = args[1];
            String playerName = cs.getName();
            Player bukkitPlayer = (Player) cs;
            UUID playerUUID = bukkitPlayer.getUniqueId();
            GuildPlayer guildPlayer = guildPlayerManager.getGuildPlayer(bukkitPlayer);

            if (guildPlayer.isInGuild()) {
                JulyMessage.sendColoredMessage(cs, "&c你已经有一个宗门或在一个宗门里了.");
                return true;
            }

            if (!guildName.matches(settings.getCreateGuildNameRegex())) {
                JulyMessage.sendColoredMessage(cs, settings.getCreateGuildNameNotValidMsg());
                return true;
            }

            String costType = args[0];

            switch (costType.toLowerCase()) {
                case "money":
                    if (!settings.isCreateGuildCostMoneyEnabled()) {
                        JulyMessage.sendColoredMessage(cs, "&e非法参数.");
                        return true;
                    }

                    double playerMoney = vault.getBalance(bukkitPlayer);

                    if (playerMoney < settings.getCreateGuildCostMoneyAmount()) {
                        JulyMessage.sendColoredMessage(cs, "&c要创建宗门, 你还需要 &e" + (settings.getCreateGuildCostMoneyAmount() - playerMoney) + "个 &c金币!");
                        return true;
                    }

                    if (guildManager.createGuild(guildPlayer, guildName)) {
                        JulyMessage.sendColoredMessage(cs, "&d恭喜 &e" + playerName + " &d创建 &e" + guildName + " &d成功!");
                        JulyMessage.sendTitle(bukkitPlayer, new TitleBuilder().text("&d创建宗门成功").colored().build());
                        return true;
                    } else {
                        JulyMessage.sendColoredMessage(cs, "&c创建宗门失败: 请联系管理员.");
                        return true;
                    }
                case "points":
                    if (!settings.isCreateGuildCostPointsEnabled()) {
                        JulyMessage.sendColoredMessage(cs, "&e非法参数.");
                        return true;
                    }

                    int playerPoints = playerPointsAPI.look(bukkitPlayer.getUniqueId());

                    if (playerPoints < settings.getCreateGuildCostMoneyAmount()) {
                        JulyMessage.sendColoredMessage(cs, "&c要创建宗门, 你还需要 &e" + (settings.getCreateGuildCostMoneyAmount() - playerPoints) + "个 &c点券!");
                        return true;
                    }

                    playerPointsAPI.take(playerUUID, settings.getCreateGuildCostPointsAmount());

                    if (guildManager.createGuild(guildPlayer, guildName)) {
                        JulyMessage.sendColoredMessage(cs, "&d恭喜 &e" + playerName + " &d创建 &e" + guildName + " &d成功!");
                        JulyMessage.sendTitle(bukkitPlayer, new TitleBuilder().text("&d创建宗门成功").colored().build());
                        return true;
                    } else {
                        JulyMessage.sendColoredMessage(cs, "&c创建宗门失败: 请联系管理员.");
                        return true;
                    }
                case "item":
                    if (!settings.isCreateGuildCostItemEnabled()) {
                        JulyMessage.sendColoredMessage(cs, "&e非法参数.");
                        return true;
                    }

                    for (ItemStack itemStack : bukkitPlayer.getInventory().getContents()) {
                        if (ItemUtil.containsLore(itemStack, settings.getCreateGuildCostItemKeyLore())) {
                            itemStack.setType(Material.AIR);

                            if (guildManager.createGuild(guildPlayer, guildName)) {
                                JulyMessage.sendColoredMessage(cs, "&d恭喜 &e" + playerName + " &d创建 &e" + guildName + " &d成功!");
                                JulyMessage.sendTitle(bukkitPlayer, new TitleBuilder().text("&d创建宗门成功").colored().build());
                                return true;
                            } else {
                                JulyMessage.sendColoredMessage(cs, "&c创建宗门失败: 请联系管理员.");
                                return true;
                            }
                        }
                    }

                    JulyMessage.sendColoredMessage(cs, "&c要创建宗门, 你还需要 &e建帮令x1&c.");
                    return true;
            }
        }
        return false;
    }

    @Override
    public String getFirstArg() {
        return "create";
    }
}
