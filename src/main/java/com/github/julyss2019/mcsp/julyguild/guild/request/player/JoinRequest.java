package com.github.julyss2019.mcsp.julyguild.guild.request.player;

import com.github.julyss2019.mcsp.julyguild.guild.request.RequestType;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;

import java.util.UUID;

public class JoinRequest extends BasePlayerRequest {
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
}
