package com.github.julyss2019.mcsp.julyguild.player.request;

import com.github.julyss2019.mcsp.julyguild.BaseRequest;

// 无意义的两次抽象，可以只保留接口或只保留抽象类 —— 754503921
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
