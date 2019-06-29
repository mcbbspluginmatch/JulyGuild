package com.github.julyss2019.mcsp.julyguild.guild.request;

import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;

import java.util.UUID;

public class BaseRequest implements Request {
    private RequestType requestType;
    private OfflineGuildPlayer offlineGuildPlayer;
    private long time;
    private UUID uuid;

    public BaseRequest(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setOfflineGuildPlayer(OfflineGuildPlayer offlineGuildPlayer) {
        this.offlineGuildPlayer = offlineGuildPlayer;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public BaseRequest(RequestType requestType, OfflineGuildPlayer offlineGuildPlayer, long time, UUID uuid) {
        this.requestType = requestType;
        this.offlineGuildPlayer = offlineGuildPlayer;
        this.time = time;
        this.uuid = uuid;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public long getCreationTime() {
        return time;
    }

    @Override
    public OfflineGuildPlayer getOfflineGuildPlayer() {
        return offlineGuildPlayer;
    }

    @Override
    public RequestType getType() {
        return requestType;
    }

    @Override
    public boolean isTimeout() {
        return false;
    }

    @Override
    public boolean isOnlyOne() {
        return false;
    }
}
