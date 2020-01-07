package com.diquemc.nick.command;

import com.diquemc.nick.Messages;
import com.diquemc.nick.manager.NickManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public class RefreshNicksCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        String result = NickManager.updateAllNicks();
        commandSender.sendMessage(Messages.PREFIX + ChatColor.GREEN + "Updated: " + result);
        return  true;
    }

}
