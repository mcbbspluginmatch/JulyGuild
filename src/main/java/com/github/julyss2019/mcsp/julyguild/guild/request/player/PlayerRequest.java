package com.github.julyss2019.mcsp.julyguild.guild.request.player;

import com.github.julyss2019.mcsp.julyguild.guild.request.RequestType;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;

import java.util.UUID;

public interface PlayerRequest {
    OfflineGuildPlayer getOfflineGuildPlayer();
    long getTime();
    RequestType getType();
    UUID getUUID();
}
