package com.diquemc.nick;
import com.diquemc.helper.DiqueMCPlugin;
import com.diquemc.nick.command.RefreshNicksCommand;
import com.diquemc.nick.command.RemoveNickCommand;
import com.diquemc.nick.command.SetNickCommand;
import com.diquemc.nick.listener.LoginListener;
import com.diquemc.nick.manager.NickManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;

import java.util.UUID;

public class DiqueMCNick extends DiqueMCPlugin {
    public static String PERMISSION_SET = "diquemcnick.set";
    public static String PERMISSION_REMOVE = "diquemcnick.set";
    public static String PERMISSION_ADMIN = "diquemcnick.admin";
    public static String ID = UUID.randomUUID().toString();

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.GREEN + "Initializing DiqueMC Nick with server uuid " + ChatColor.YELLOW + ID);
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new LoginListener(), this);

        initializeCommand("nick", new SetNickCommand());
        initializeCommand("removenick", new RemoveNickCommand());
        initializeCommand("nicksreload", new RefreshNicksCommand());
        NickManager.enableManager();
    }

    public void onDisable() {
        NickManager.disableManager();

    }
}
