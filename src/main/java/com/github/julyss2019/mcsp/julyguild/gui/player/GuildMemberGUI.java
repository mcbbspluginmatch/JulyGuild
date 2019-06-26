package com.github.julyss2019.mcsp.julyguild.gui.player;

import com.github.julyss2019.mcsp.julyguild.gui.BasePageableGUI;
import com.github.julyss2019.mcsp.julyguild.guild.Guild;
import com.github.julyss2019.mcsp.julyguild.player.GuildPlayer;

public class GuildMemberGUI extends BasePageableGUI {
    private Guild guild;

    public GuildMemberGUI(Guild guild, GuildPlayer guildPlayer) {
        super(guildPlayer);

        this.guild = guild;
    }

    @Override
    public void setCurrentPage(int page) {
        super.setCurrentPage(page);
    }
}
