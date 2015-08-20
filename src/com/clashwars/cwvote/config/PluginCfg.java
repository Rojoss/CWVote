package com.clashwars.cwvote.config;

import com.clashwars.cwcore.config.internal.EasyConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class PluginCfg extends EasyConfig {

    public String SQL_PASS = "SECRET";
    public String SERVER = "dvz";

    public PluginCfg(String fileName) {
        this.setFile(fileName);
    }

}
