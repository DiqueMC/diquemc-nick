package com.diquemc.nick.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class PlayerManager {
      public static String checkValidPlayer (String playerName) {
        String playerUUID = com.diquemc.utils.manager.PlayerManager.getPlayerUUID(playerName);
        if(playerUUID == null) {
            return  null;
        }
        Map<String,String> playerData = com.diquemc.utils.manager.PlayerManager.getPlayerData(playerUUID);
        if(playerData.get("name") != null) {
            return playerData.get("name");
        }
        return playerName;
    }

    public static Player getOnlinePlayerWithName (String playerName) {
        Player player = Bukkit.getServer().getPlayer(playerName);
        if(player != null && player.isOnline()) {
            return player;
        }
        return null;
    }
}
