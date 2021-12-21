package me.trade.vivace;

import net.md_5.bungee.api.ChatColor;

public class ColorString {

    public static ColorString valueOf(String s) {
        return new ColorString(s);
    }

    public static String colored(String s) {
        return String.valueOf(s).replace("&", "§");
    }

    public static String uncolored(String s) {
        return ChatColor.stripColor(String.valueOf(s));
    }

    private final String s;

    private ColorString(String s) {
        this.s = s;
    }

    public String getUncolored() {
        return ChatColor.stripColor(String.valueOf(s));
    }

    @Override
    public String toString() {
        return String.valueOf(s).replace("&", "§");
    }
}
