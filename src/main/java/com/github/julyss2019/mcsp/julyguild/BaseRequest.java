package com.github.julyss2019.mcsp.julyguild;

import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;

import java.util.UUID;

public class BaseRequest implements Request {
    private OfflineGuildPlayer requester;
    private long time;
    private UUID uuid;

    public BaseRequest() {
    }

    public BaseRequest(OfflineGuildPlayer requester, long time, UUID uuid) {
        this.requester = requester;
        this.time = time;
        this.uuid = uuid;
    }

    public void setRequester(OfflineGuildPlayer requester) {
        this.requester = requester;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getTime() {
        return time;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public OfflineGuildPlayer getRequester() {
        return requester;
    }

    @Override
    public long getCreationTime() {
        return time;
    }

    @Override
    public boolean isTimeout() {
        return false;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
