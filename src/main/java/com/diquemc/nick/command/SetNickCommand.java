package com.diquemc.nick.command;

import com.diquemc.nick.DiqueMCNick;
import com.diquemc.nick.Messages;
import com.diquemc.nick.manager.NickManager;
import com.diquemc.nick.manager.PlayerManager;
import com.diquemc.utils.ChatUtil;
import com.diquemc.utils.DiqueMCUtils;
import me.winterguardian.jsonconverter.JsonMessageSender;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SetNickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, String[] args) {
        if (args.length == 1 && commandSender instanceof Player) {
            return runAsPlayer((Player) commandSender, args[0]);
        } else if (args.length == 2 && commandSender instanceof Player) {
            return runAsAdmin(commandSender, args[0], args[1]);
        }
        return showHelp(commandSender);
    }

    private boolean isAllowedNick(String playerName, String nickName) {
        String nick = ChatUtil.translateColorCodes(nickName);
        nick = nick.replace(ChatColor.MAGIC.toString(), "&k");
        nick = ChatColor.stripColor(nick);
        return nick.toLowerCase().contains(playerName.toLowerCase());
    }

    private String processNick(String nickName) {
        return ChatUtil.transformHexColor(nickName);
    }

    private boolean runAsPlayer(Player player, String nickName) {
        String playerName = player.getName();
        if (!player.hasPermission(DiqueMCNick.PERMISSION_SET)) {
            player.sendMessage(Messages.NO_COMMAND_PERMISSION);
            return true;
        }
        if(nickName.equals(playerName)) {
            return new RemoveNickCommand().runAsPlayer(player);
        }
        nickName = processNick(nickName);
        if (!isAllowedNick(playerName, nickName)) {
            player.sendMessage(Messages.NICK_DOESNT_CONTAIN_NAME);
            return true;
        }
        NickManager.setNickForPlayer(playerName, nickName);
        player.sendMessage(Messages.NICK_CHANGED(nickName));
        return true;
    }

    private boolean runAsAdmin(CommandSender commandSender, String _playerName, String nickName) {
        if(commandSender instanceof Player && !commandSender.hasPermission(DiqueMCNick.PERMISSION_ADMIN)) {
            commandSender.sendMessage(Messages.NO_COMMAND_PERMISSION);
            showHelp(commandSender);
            return true;
        }
        String playerName = PlayerManager.checkValidPlayer(_playerName);
        if(playerName == null) {
            commandSender.sendMessage(Messages.PLAYER_NOT_FOUND(_playerName));
            return  true;
        }
        if(nickName.equals(playerName)) {
            return new RemoveNickCommand().runAsAdmin(commandSender, playerName);
        }
        nickName = processNick(nickName);
        NickManager.setNickForPlayer(playerName, nickName);
        commandSender.sendMessage(Messages.NICK_CHANGED(playerName, nickName));
        return true;
    }

    private boolean showHelp(CommandSender commandSender) {
        commandSender.sendMessage(Messages.PREFIX + ChatColor.RED + "Uso del comando: /nick <tu nuevo nick>");
        commandSender.sendMessage(ChatColor.YELLOW + "Para poner un color o formato, debes usar el simbolo " + ChatColor.AQUA + "&" +
                ChatColor.YELLOW + " junto con su codigo correspondiente: ");
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('%', "%0 &0  %1 &1  %2 &2  %3 &3"));
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('%', "%4 &4  %5 &5  %6 &6  %7 &7"));
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('%', "%8 &8  %9 &9  %a &a  %b &b"));
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('%', "%c &c  %d &d  %e &e  %f &f"));
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('%', "&k %kMagic%r"));
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('%', "&l %lNegrita%r"));
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('%', "&m %mTachado%r"));
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('%', "&n %nSubrayado"));
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('%', "&o %oItalico%r"));
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('%', "&r %rNormal"));
        commandSender.sendMessage(" ");
        if(commandSender instanceof Player) {
            String currentNick = NickManager.getNickForPlayer(commandSender.getName());
            if(currentNick == null) {
                commandSender.sendMessage(ChatColor.YELLOW + "Por ejemplo, si quieres tu nombre en azul escribe " + ChatColor.AQUA
                        + "/nick &1" + commandSender.getName() + ChatColor.YELLOW + " y tu nick sera cambiado a:  " + ChatColor.BLUE + commandSender.getName());
                commandSender.sendMessage(ChatColor.YELLOW + "No tienes un nick asignado actualmente");
            } else {
                String message = "[{\"text\":\"Tu nick actual es: \",\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click para copiar el comando\",\"color\":\"green\"},\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/nick {currentNick}\"},\"extra\":[{\"text\":\"{currentNickConverted}\",\"color\":\"aqua\"},{\"text\":\"(\",\"color\":\"green\"},{\"text\":\"/nick {currentNick}\",\"color\":\"aqua\"},{\"text\":\")\",\"color\":\"green\"}]}]";
                message = message.replace("{currentNick}", ChatUtil.reverseTransformHexColor(currentNick));
                message = message.replace("{currentNickConverted}", ChatUtil.translateColorCodes(currentNick));
//                String message = ChatColor.GREEN + "Tu nick actual es: " + ChatColor.AQUA + ChatColor.translateAlternateColorCodes('&', currentNick) + ChatColor.GREEN + "(" + ChatColor.AQUA + "/nick " + currentNick + ChatColor.GREEN + ")";
//                String json = JsonConverter.toJson(message, "show_text", ChatColor.GREEN + "Click para copiar el comando", "suggest_command", "/nick " + currentNick, "");
                JsonMessageSender.sendSafeJsonChatMessage((Player) commandSender, message);

//                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + commandSender.getName() + " " + one);
//                commandSender.sendMessage(ChatColor.GREEN + "Tu nick actual es: " + ChatColor.AQUA + ChatColor.translateAlternateColorCodes('&', currentNick) + ChatColor.GREEN + "(" + ChatColor.AQUA + "/nick " + currentNick + ChatColor.GREEN + ")");
            }
        }

        return true;
    }


}
