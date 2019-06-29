package com.github.julyss2019.mcsp.julyguild.guild.request;

import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;

import java.util.UUID;

public interface Request {
    OfflineGuildPlayer getOfflineGuildPlayer();
    long getCreationTime();
    RequestType getType();
    UUID getUUID();
    boolean isOnlyOne();
    boolean isTimeout();
}
