package com.diquemc.nick.command;

import com.diquemc.nick.DiqueMCNick;
import com.diquemc.nick.Messages;
import com.diquemc.nick.manager.NickManager;
import com.diquemc.nick.manager.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveNickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 0 && commandSender instanceof Player) {
            return runAsPlayer((Player) commandSender);
        } else if (args.length == 1) {
            return runAsAdmin(commandSender, args[0]);
        }
        return  false;
    }

      boolean runAsPlayer(Player player) {
        String playerName = player.getName();
        if (!player.hasPermission(DiqueMCNick.PERMISSION_REMOVE)) {
            player.sendMessage(Messages.NO_COMMAND_PERMISSION);
            return true;
        }
        NickManager.removeNickForPlayer(playerName);
        player.sendMessage(Messages.NICK_REMOVED);
        return true;
    }

    boolean runAsAdmin(CommandSender commandSender, String _playerName) {
        if(commandSender instanceof Player && !commandSender.hasPermission(DiqueMCNick.PERMISSION_ADMIN)) {
            commandSender.sendMessage(Messages.NO_COMMAND_PERMISSION);
            return true;
        }
        String playerName = PlayerManager.checkValidPlayer(_playerName);
        if(playerName == null) {
            commandSender.sendMessage(Messages.PLAYER_NOT_FOUND(_playerName));
        }
        NickManager.removeNickForPlayer(playerName);
        commandSender.sendMessage(Messages.NICK_REMOVED(playerName));
        return true;
    }
}
