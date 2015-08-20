package com.clashwars.cwvote;

import com.clashwars.clashwars.ClashWars;
import com.clashwars.cwcore.CWCore;
import com.clashwars.cwcore.mysql.MySQL;
import com.clashwars.cwvote.commands.Commands;
import com.clashwars.cwvote.config.PluginCfg;
import com.clashwars.cwvote.shop.ShopMenu;
import com.clashwars.cwvote.tokens.TokenManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.logging.Logger;

public class CWVote extends JavaPlugin {

    private static CWVote instance;
    private ClashWars cw;
    private CWCore cwcore;

    private PluginCfg cfg;

    private MySQL sql;
    private Connection c;

    public TokenManager tm;
    public ShopMenu shop;

    private Commands cmds;

    private final Logger log = Logger.getLogger("Minecraft");


    @Override
    public void onDisable() {
        log("disabled");
    }

    @Override
    public void onEnable() {
        Long t = System.currentTimeMillis();
        instance = this;

        Plugin plugin = getServer().getPluginManager().getPlugin("CWCore");
        if (plugin == null || !(plugin instanceof CWCore)) {
            log("CWCore dependency couldn't be loaded!");
            setEnabled(false);
            return;
        }
        cwcore = (CWCore)plugin;

        plugin = getServer().getPluginManager().getPlugin("ClashWars");
        if (plugin == null || !(plugin instanceof ClashWars)) {
            log("ClashWars dependency couldn't be loaded!");
            setEnabled(false);
            return;
        }
        cw = (ClashWars)plugin;

        cfg = new PluginCfg("plugins/CWVote/CWVote.yml");
        cfg.load();

        sql = new MySQL(this, "37.26.106.5", "3306", "clashwar_vote", "clashwar_main", cfg.SQL_PASS);
        try {
            c = sql.openConnection();
        } catch(Exception e) {
            log("##############################################################");
            log("Unable to connect to MySQL!");
            log("##############################################################");
            setEnabled(false);
            return;
        }

        tm = new TokenManager(this);

        shop = new ShopMenu(this);

        registerEvents();

        cmds = new Commands(this);

        log("loaded successfully");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return cmds.onCommand(sender, cmd, label, args);
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(shop, this);
    }


    public void log(Object msg) {
        log.info("[CWVote " + getDescription().getVersion() + "] " + msg.toString());
    }

    public void logError(Object msg) {
        log.severe("[CWVote " + getDescription().getVersion() + "] " + msg.toString());
    }

    public static CWVote inst() {
        return instance;
    }

    public CWCore getCore() {
        return cwcore;
    }

    public ClashWars getCW() {
        return cw;
    }


    public PluginCfg getCfg() {
        return cfg;
    }


    public Connection getSql() {
        return c;
    }
}
