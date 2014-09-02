package com.zack649.superlogger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Created by zack6849 on 9/1/14.
 */
public class Main extends JavaPlugin {
    private Logger logger;
    @Override
    public void onEnable() {
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("sl")){
            if(args.length > 1){
                sender.sendMessage(ChatColor.RED + "Usage: /sl <version, reload>");
                return true;
            }
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("version")){
                    sender.sendMessage(ChatColor.YELLOW + "SuperLogger Version " + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors().toString());
                }
                if(args[0].equalsIgnoreCase("reload")){
                    if(sender.hasPermission("sl.reload")){
                        this.reloadConfig();
                        sender.sendMessage(ChatColor.GREEN + "Configuration file reloaded!");
                    }
                }
            }
        }
        return false;
    }
}
