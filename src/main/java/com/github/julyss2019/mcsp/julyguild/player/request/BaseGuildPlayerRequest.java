package com.github.julyss2019.mcsp.julyguild.player.request;

import com.github.julyss2019.mcsp.julyguild.BaseRequest;

public class BaseGuildPlayerRequest extends BaseRequest implements GuildPlayerRequest {
    private GuildPlayerRequestType type;

    public BaseGuildPlayerRequest(GuildPlayerRequestType type) {
        this.type = type;
    }

    @Override
    public GuildPlayerRequestType getType() {
        return type;
    }
}
