package com.diquemc.nick.manager;

import com.diquemc.jedis.DiqueMCJedis;
import com.diquemc.jedis.DiqueMCJedisListener;
import com.diquemc.jedis.DiqueMCJedisPubSub;
import com.diquemc.nick.DiqueMCNick;
import com.diquemc.utils.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class NickManager implements DiqueMCJedisListener {
    private static final String CHANEL_NICKMANAGER = "DiqueMCNick";
    private static final String REDIS_KEY = "diquemc-nick";

    public static String getNickForPlayer(String playerName) {
        return DiqueMCJedis.hget(REDIS_KEY, playerName.toLowerCase());
    }

    public static void setNickForPlayer(String playerName, String nickName) {
        DiqueMCJedis.hset(REDIS_KEY, playerName.toLowerCase(), nickName);
        updateNick(playerName, nickName);
    }

    public static void removeNickForPlayer(String playerName) {
        DiqueMCJedis.hdel(REDIS_KEY, playerName.toLowerCase());
        updateNick(playerName, playerName);
    }

    private static String getParsedNickName(String nickNameString) {
        String nickName = ChatUtil.translateColorCodes(nickNameString);
        if (nickName.charAt(nickName.length() - 1) == '§') {
            nickName = nickName.substring(0, nickName.length() - 1);
        }
        nickName = nickName + ChatColor.RESET;
        return nickName;
    }

    //List name is limited to 15 characters
    private static String getParsedNickNameForList(String nickNameString) {
//        if (nickNameString.length() > 16) {
//                nickNameString = nickNameString.substring(0, 15);
//        }
        return getParsedNickName(nickNameString);
    }

    public static void initializePlayer(Player player, boolean isNickUpdate) {
        if(!player.hasPermission(DiqueMCNick.PERMISSION_SET)) {
            return;
        }
        String nickName = getNickForPlayer(player.getName());
        if(nickName == null) {
            nickName = player.getName();
        }
        nickName = getParsedNickName(nickName);
        String listNickName = getParsedNickNameForList(nickName);
        player.setDisplayName(nickName);
        player.setPlayerListName(listNickName);
        if(!isNickUpdate){
            publishNick(player.getName(), nickName);
        }
    }

    private static NickManager instance = null;
    private static DiqueMCJedisPubSub pubSubListener;
    public static void enableManager() {
        disableManager();
        if(instance == null) {
            instance = new NickManager();
        }
        pubSubListener = new DiqueMCJedisPubSub(instance);
        pubSubListener.subscribeToChannel(CHANEL_NICKMANAGER);

    }

    public static void disableManager() {
        if(pubSubListener != null) {
            pubSubListener.unsubscribe();
        }
    }

    private static void publishNick(String playerName, String nickName) {
        DiqueMCJedis.publish(CHANEL_NICKMANAGER, playerName + "\r" + nickName+ "\r" + DiqueMCNick.ID);
//        DiqueMCJedis.publish("DiqueMCChatNick", playerName + "\r" + nickName + "\r" + DiqueMCNick.ID);
    }

    private static void updateNick(String playerName, String nickName) {
        handleNickUpdate(playerName);
        publishNick(playerName, nickName);
    }

    private static void handleNickUpdate(String playerName) {
        Player player = PlayerManager.getOnlinePlayerWithName(playerName);
        if(player != null) {
            initializePlayer(player, true);
        }
    }

    public static String updateAllNicks() {
        Map<String, String> allNicks = DiqueMCJedis.hgetall(REDIS_KEY);
        List<String> nicks = new ArrayList<>();
        for( Map.Entry<String,String> player : allNicks.entrySet()) {
            updateNick(player.getKey(), player.getValue());
            nicks.add(ChatUtil.translateColorCodes(player.getValue()));
        }
        return String.join(ChatColor.YELLOW + ", ", nicks);
    }

    @Override
    public void onMessage(String channel, String message, String[] parsedMessage) {
        //Ignore the message if it was generated within the current server
        if(parsedMessage == null || parsedMessage.length == 0) {
            return;
        }
        if(parsedMessage.length >= 3 && DiqueMCNick.ID.equalsIgnoreCase(parsedMessage[2])) {
            return;
        }
        handleNickUpdate(parsedMessage[0]);
    }

}
