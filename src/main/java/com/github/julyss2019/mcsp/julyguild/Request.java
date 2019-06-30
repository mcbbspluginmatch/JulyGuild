package com.github.julyss2019.mcsp.julyguild;

import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;

import java.util.UUID;

public interface Request {
    OfflineGuildPlayer getRequester();
    long getCreationTime();
    boolean isTimeout();
    UUID getUUID();
}
