package com.diquemc.nick;
import com.diquemc.nick.command.RefreshNicksCommand;
import com.diquemc.nick.command.RemoveNickCommand;
import com.diquemc.nick.command.SetNickCommand;
import com.diquemc.nick.listener.LoginListener;
import com.diquemc.nick.manager.NickManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class DiqueMCNick extends JavaPlugin {
    public static String PERMISSION_SET = "diquemcnick.set";
    public static String PERMISSION_REMOVE = "diquemcnick.set";
    public static String PERMISSION_ADMIN = "diquemcnick.admin";
    public static String ID = UUID.randomUUID().toString();

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.GREEN + "Initializing DiqueMC Nick with server uuid " + ChatColor.YELLOW + ID);
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new LoginListener(), this);

        getCommand("nick").setExecutor(new SetNickCommand());
        getCommand("removenick").setExecutor(new RemoveNickCommand());
        getCommand("nicksreload").setExecutor(new RefreshNicksCommand());
        NickManager.enableManager();
    }

    public void onDisable() {
        NickManager.disableManager();

    }
}
