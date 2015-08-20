package com.clashwars.cwvote.tokens;

import com.clashwars.cwvote.CWVote;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class TokenManager {

    private CWVote cwv;

    public TokenManager(CWVote cwv) {
        this.cwv = cwv;
    }


    public int getTokens(OfflinePlayer player) {
        int charID = cwv.getCW().um.getUser(player).getCharID();;
        if (cwv.getSql() != null) {
            try {
                Statement checkStatement = cwv.getSql().createStatement();
                ResultSet tokens = checkStatement.executeQuery("SELECT tokens FROM VoteTokens WHERE char_id=" + charID + ";");

                if (tokens.next()) {
                    return tokens.getInt("tokens");
                }
            } catch (SQLException e) {
                cwv.log("Failed to load tokens from database 2!");
            }
        } else {
            cwv.log("Failed to load tokens from database 1!");
        }
        return -1;
    }


    public void setTokens(OfflinePlayer player, final int amount) {
        final int charID = cwv.getCW().um.getUser(player).getCharID();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (cwv.getSql() != null) {
                    try {
                        Statement checkStatement = cwv.getSql().createStatement();
                        ResultSet tokens = checkStatement.executeQuery("SELECT tokens FROM VoteTokens WHERE char_id=" + charID + ";");

                        if (tokens.next()) {
                            PreparedStatement updateStatement = cwv.getSql().prepareStatement("UPDATE VoteTokens SET tokens=? WHERE char_id=?;");
                            updateStatement.setInt(1, amount);
                            updateStatement.setInt(2, charID);
                            int added = updateStatement.executeUpdate();
                            if (added <= 0) {
                                cwv.log("Failed to insert user/tokens in database 3!");
                            }
                        } else {
                            Statement insertStatement = cwv.getSql().createStatement();
                            int added = insertStatement.executeUpdate("INSERT INTO VoteTokens (char_id,tokens) VALUES (" + charID + "," + amount + ");");
                            if (added <= 0) {
                                cwv.log("Failed to insert user/tokens in database 4!");
                            }
                        }
                    } catch (SQLException e) {
                        cwv.log("Failed to update tokens in database 2!");
                    }
                } else {
                    cwv.log("Failed to update tokens in database 1!");
                }
            }
        }.runTaskAsynchronously(cwv);
    }


    public void changeTokens(OfflinePlayer player, int amount) {
        setTokens(player, Math.max(0,getTokens(player)) + amount);
    }
}
