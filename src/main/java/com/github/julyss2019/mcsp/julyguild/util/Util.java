package com.github.julyss2019.mcsp.julyguild.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static SimpleDateFormat YMD_SDF = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat YMDHM_SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public static List<String> repalceVarialbe(List<String> lores, String var, String value) {
        List<String> result = new ArrayList<>();

        for (String lore : lores) {
            result.add(lore.replace(var, value));
        }

        return result;
    }

    public static String getTimeLeftStr(long timeLeft) {
        long h = timeLeft / 60 / 60;
        long m = timeLeft / 60;
        long s = timeLeft % 60;

        return (h == 0 ? "" : h + "时") + (m == 0 ? "" : m + "分" + (s == 0 ? "钟" : "")) + (s == 0 && (h != 0 || m != 0) ? "" : s + "秒");
    }
}
