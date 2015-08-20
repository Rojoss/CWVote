package com.clashwars.cwvote.commands;

import com.clashwars.cwcore.utils.CWUtil;
import com.clashwars.cwvote.CWVote;
import com.clashwars.cwvote.util.Util;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {

    private CWVote cwv;

    public Commands(CWVote cwv) {
        this.cwv = cwv;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (label.equalsIgnoreCase("tokens")) {
            if (!sender.isOp() && !sender.hasPermission("cwvote.tokens")) {
                sender.sendMessage(Util.formatMsg("&cInsufficient permissions!"));
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(Util.formatMsg("&cInvalid usage. &7/" + label + " {add|set|remove|get} [player] [amt]"));
                return true;
            }

            OfflinePlayer player = cwv.getServer().getOfflinePlayer(args[1]);
            if (player == null || !player.hasPlayedBefore()) {
                sender.sendMessage(Util.formatMsg("&cInvalid player specified!"));
                return true;
            }

            int amount = 1;
            if (args.length >= 3) {
                amount = Math.max(CWUtil.getInt(args[2]), 1);
            }


            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("give")) {
                cwv.tm.changeTokens(player, amount);
                return true;
            }

            if (args[0].equalsIgnoreCase("set")) {
                cwv.tm.setTokens(player, amount);
                return true;
            }

            if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("take")) {
                cwv.tm.changeTokens(player, -amount);
                return true;
            }

            if (args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("show")) {
                sender.sendMessage(Util.formatMsg("&6" + player.getName() + " has &a&l" + cwv.tm.getTokens(player) + " &atokens"));
                return true;
            }
        }

        if (label.equalsIgnoreCase("voteshop") || label.equalsIgnoreCase("tokenshop")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Util.formatMsg("&cPlayer command only."));
                return true;
            }

            cwv.shop.showMenu((Player)sender);
        }

        if (label.equalsIgnoreCase("vote")) {
            sender.sendMessage(CWUtil.integrateColor("&8---"));
            sender.sendMessage(CWUtil.integrateColor("&6&lYou can vote &a&ldaily &6&lon the links below!"));
            sender.sendMessage(CWUtil.integrateColor("&7You'll receive a vote token for each site you vote on!"));
            sender.sendMessage(CWUtil.integrateColor("&7Spend the vote tokens in the vote shop. &b/voteshop"));
            sender.sendMessage(CWUtil.integrateColor("&7Please also give a &bdiamond &7and &cfavorite &7on PMC. &4<3"));
            sender.sendMessage("");
            sender.sendMessage(CWUtil.integrateColor("&6Planet Minecraft&8: &9http://goo.gl/k2sLRc"));
            sender.sendMessage(CWUtil.integrateColor("&6MinecraftServers&8: &9http://goo.gl/8OVX0G"));
            sender.sendMessage(CWUtil.integrateColor("&6Minecraft-Server-List&8: &9http://goo.gl/0MtCI0"));
            sender.sendMessage(CWUtil.integrateColor("&8---"));
            return true;
        }



        return true;
    }

}
