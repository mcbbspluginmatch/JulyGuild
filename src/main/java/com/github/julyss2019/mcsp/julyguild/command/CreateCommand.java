package com.github.julyss2019.mcsp.julyguild.command;

import org.bukkit.command.CommandSender;

public class CreateCommand implements Command {
    @Override
    public boolean onCommand(CommandSender cs, String[] args) {
        if (args.length == 2) {
            String guildName = args[1];



            String costType = args[0];

            switch (costType) {
                case "money":

            }
        }
        return false;
    }

    @Override
    public String getFirstArg() {
        return "create";
    }
}
