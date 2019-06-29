package com.github.julyss2019.mcsp.julyguild.guild.request;

import com.github.julyss2019.mcsp.julyguild.JulyGuild;
import com.github.julyss2019.mcsp.julyguild.config.GuildSettings;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;

import java.util.UUID;

public class JoinRequest extends BaseRequest implements Request {
    private static JulyGuild plugin = JulyGuild.getInstance();
    private static GuildSettings guildSettings = plugin.getGuildSettings();

    public JoinRequest() {
        super(RequestType.JOIN);
    }

    public static JoinRequest createNew(OfflineGuildPlayer offlineGuildPlayer) {
        JoinRequest joinRequest = new JoinRequest();

        joinRequest.setTime(System.currentTimeMillis());
        joinRequest.setOfflineGuildPlayer(offlineGuildPlayer);
        joinRequest.setUUID(UUID.randomUUID());
        return joinRequest;
    }

    @Override
    public boolean isTimeout() {
        return (System.currentTimeMillis() - getCreationTime()) / 1000 > guildSettings.getGuildRequestJoinTimeout();
    }

    @Override
    public boolean isOnlyOne() {
        return true;
    }
}
