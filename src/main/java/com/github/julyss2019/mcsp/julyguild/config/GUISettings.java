package com.github.julyss2019.mcsp.julyguild.config;

import com.github.julyss2019.mcsp.julylibrary.config.Config;

import java.util.List;

public class GUISettings {
    @Config(path = "global.guild_info.display_name")
    private String globalGuildInfoDisplayName;
    @Config(path = "global.guild_info.lores")
    private List<String> globalGuildInfoLores;

    @Config(path = "main_gui.ranking_list.display_name")
    private String mainGUIRankingListDisplayName;
    @Config(path = "main_gui.ranking_list.lores")
    private List<String> mainGUIRankingListLores;

    @Config(path = "mine_gui.player_info.display_name")
    private String mineGUIPlayerInfoDisplayName;
    @Config(path = "mine_gui.player_info.lores")
    private List<String> mineGUIPlayerInfoLores;

    public String getGlobalGuildInfoDisplayName() {
        return globalGuildInfoDisplayName;
    }

    public List<String> getGlobalGuildInfoLores() {
        return globalGuildInfoLores;
    }

    public String getMainGUIRankingListDisplayName() {
        return mainGUIRankingListDisplayName;
    }

    public List<String> getMainGUIRankingListLores() {
        return mainGUIRankingListLores;
    }

    public String getMineGUIPlayerInfoDisplayName() {
        return mineGUIPlayerInfoDisplayName;
    }

    public List<String> getMineGUIPlayerInfoLores() {
        return mineGUIPlayerInfoLores;
    }
}
