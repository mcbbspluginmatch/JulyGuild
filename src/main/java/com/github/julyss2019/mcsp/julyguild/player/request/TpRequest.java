package com.github.julyss2019.mcsp.julyguild.player.request;

import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;
import com.github.julyss2019.mcsp.julyguild.player.OfflineGuildPlayer;
import org.bukkit.Location;

import java.util.UUID;

public class TpRequest extends BaseGuildPlayerRequest {
    private Location location;

    public TpRequest(Location location) {
        super(GuildPlayerRequestType.TP);

        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public static TpRequest createNew(OfflineGuildPlayer requester, Location location) {
        TpRequest instance = new TpRequest(location);

        instance.setRequester(requester);
        instance.setTime(System.currentTimeMillis());
        instance.setUuid(UUID.randomUUID());
        return instance;
    }
}
