package com.github.julyss2019.mcsp.julyguild;

import com.github.julyss2019.mcsp.julyguild.guild.CacheGuildManager;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayerManager;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;
import com.github.julyss2019.mcsp.julyguild.util.Util;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * PAPI扩张
 */
public class PlaceholderAPIExpansion extends PlaceholderExpansion {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static CacheGuildManager cacheGuildManager = plugin.getCacheGuildManager();
    private static GuildPlayerManager guildPlayerManager = plugin.getGuildPlayerManager();

    @Override
    public String getIdentifier() {
        return "GUILD";
    }

    @Override
    public String getAuthor() {
        return "July_ss";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        String playerName = p.getName();
        OfflineGuildPlayer offlineGuildPlayer = guildPlayerManager.getOfflineGuildPlayer(playerName);
        Guild guild = offlineGuildPlayer.getGuild();
        boolean isInGuild = guild != null;

        if (params.equalsIgnoreCase("is_in_guild")) {
            return String.valueOf(isInGuild);
        }

        if (!isInGuild) {
            return "-";
        }

        switch (params.toLowerCase()) {
            case "name":
                return guild.getName();
            case "member_per":
                return guild.getMember(playerName).getPermission().getChineseName();
            case "member_donate_money":
                return String.valueOf(guild.getMember(playerName).getDonatedBalance());
            case "member_join_time":
                return Util.YMD_SDF.format(guild.getMember(playerName).getJoinTime());
            case "ranking":
                return String.valueOf(cacheGuildManager.getRanking(guild));
            case "owner":
                return guild.getOwner().getName();
            case "member_count":
                return String.valueOf(guild.getMemberCount());
            case "max_member_count":
                return String.valueOf(guild.getMaxMemberCount());
            case "creation_time":
                return Util.YMD_SDF.format(guild.getCreationTime());
            case "balance":
                return String.valueOf((int) guild.getBalance());
        }

        return null;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        return onPlaceholderRequest(p, params);
    }

    @Override
    public boolean canRegister() {
        return true;
    }
}
