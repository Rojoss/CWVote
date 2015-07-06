package com.clashwars.cwvote.commands;

import com.clashwars.cwvote.CWVote;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Commands {

    private CWVote cwv;

    public Commands(CWVote cwv) {
        this.cwv = cwv;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return true;
    }

}
