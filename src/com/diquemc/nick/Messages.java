package com.diquemc.nick;


import org.bukkit.ChatColor;

public class Messages {
    public static String PREFIX = ChatColor.GOLD + "[DiqueMC Nick] " + ChatColor.RESET;
    public static String NO_COMMAND_PERMISSION = PREFIX + ChatColor.RED + "No tienes permisos para ejecutar este comando";
    public static String NICK_DOESNT_CONTAIN_NAME = PREFIX + ChatColor.RED + "Tu nick debe contener tu nombre real";
    public static String PLAYER_NOT_FOUND(String playerName) {
        return PREFIX + ChatColor.RED + "No se ha podido encontrar un jugador con el nombre: " + ChatColor.YELLOW + playerName;
    }

    public static String NICK_CHANGED (String nickName) {
        return PREFIX + ChatColor.GREEN + "Tu nickname ha sido cambiado a " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', nickName) + ChatColor.GREEN + "!";
    }
    public static String NICK_CHANGED (String playerName, String nickName) {
        return PREFIX + ChatColor.GREEN + "El nickname de " + ChatColor.YELLOW + playerName + ChatColor.GREEN + " ha sido cambiado a " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', nickName) + ChatColor.GREEN + "!";
    }

    public static String NICK_REMOVED = ChatColor.GREEN + "Tu nick ha sido removido";
    public static String NICK_REMOVED(String playerName){
        return PREFIX + ChatColor.GREEN + "El nick de " + ChatColor.YELLOW + playerName + ChatColor.GREEN + " ha sido removido";
    }

}
