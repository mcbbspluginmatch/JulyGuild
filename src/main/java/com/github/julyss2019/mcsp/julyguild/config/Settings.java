package com.github.julyss2019.mcsp.julyguild.config;

import com.github.julyss2019.mcsp.julylibrary.config.Config;

import java.util.List;

public class Settings {
    @Config(path = "player_info_item_lores")
    private List<String> playerInfoItemLores;

    public List<String> getPlayerInfoItemLores() {
        return playerInfoItemLores;
    }
}
