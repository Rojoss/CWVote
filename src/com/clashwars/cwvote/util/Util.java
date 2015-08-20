package com.clashwars.cwvote.util;

import com.clashwars.cwcore.utils.CWUtil;

public class Util {
    public static String formatMsg(String msg) {
        return CWUtil.integrateColor("&8[&4CWVote&8] &6" + msg);
    }
}
