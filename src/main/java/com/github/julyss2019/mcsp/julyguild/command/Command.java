package com.github.julyss2019.mcsp.julyguild.command;

import com.github.julyss2019.mcsp.julylibrary.command.JulyCommand;

public interface Command extends JulyCommand {
    @Override
    default boolean isOnlyPlayerCanUse() {
        return true;
    }

    @Override
    default String getPermission() {
        return "JulyGuild.use";
    }
}
