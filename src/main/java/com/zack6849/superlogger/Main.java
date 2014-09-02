package com.zack6849.superlogger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zack6849 on 9/1/14.
 */
public class Main extends JavaPlugin {
    private Logger logger;
    private Settings settings;
    private HashMap<String, LoggerAbstraction> loggers;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        if (this.getConfig().getConfigurationSection("log") == null) {
            logger.log(Level.SEVERE, "Your configuration file is out of date, please generate a new one!");
            logger.log(Level.INFO, "Disabling Superlogger...");
            getPluginLoader().disablePlugin(this);
        }
        this.loggers = new HashMap<String, LoggerAbstraction>();
    }

    public void loadSettings() {
        this.settings = new Settings();
        settings.setLogChat(getConfig().getBoolean("log.chat"));
        settings.setLogCommands(getConfig().getBoolean("log.commands"));
        settings.setLogCoordinates(getConfig().getBoolean("log.coordinates"));
        settings.setLogDeath(getConfig().getBoolean("log.death"));
        settings.setLogJoin(getConfig().getBoolean("log.join"));
        settings.setLogQuit(getConfig().getBoolean("log.quit"));
        settings.setLogKick(getConfig().getBoolean("log.kick"));
        settings.setLogDisallowedConnections(getConfig().getBoolean("log.failed-connections"));
        settings.setLogPlayerIp(getConfig().getBoolean("log.player-ip"));
        settings.setLogPlayerUUID(getConfig().getBoolean("log.player-uuid"));
        settings.setCheckCommandExists(getConfig().getBoolean("log.check-command-exists"));
        settings.setFilteredCommands(new HashSet<String>());
        for (String command : getConfig().getStringList("blacklist")) {
            //lowercase all commands so it's easier to check
            settings.getFilteredCommands().add(command.toLowerCase());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Removing loggers and closing files!");
        for (String loggername : loggers.keySet()) {
            try {
                LoggerAbstraction logger = loggers.get(loggername);
                logger.getWriter().flush();
                logger.getWriter().close();
                loggers.remove(loggername);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sl")) {
            if (args.length > 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /sl <version, reload>");
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("version")) {
                    sender.sendMessage(ChatColor.YELLOW + "SuperLogger Version " + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors().toString());
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("sl.reload")) {
                        this.reloadConfig();
                        loadSettings();
                        sender.sendMessage(ChatColor.GREEN + "Configuration file reloaded!");
                    }
                }
            }
        }
        return false;
    }

    public void log(String line, LoggingCategory category) {
        BufferedWriter writer = getFile(category).getWriter();
        try {
            writer.write(getTime() + line);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            logger.severe("Error writing line to log category " + category.toString());
            e.printStackTrace();
        }
    }

    public LoggerAbstraction getFile(LoggingCategory category) {
        String filename = category.getFileName();
        if (!loggers.containsKey(filename)) {
            getLogger().info("Creating new LoggerAbstraction for category " + category.toString());
            LoggerAbstraction log = new LoggerAbstraction();
            try {
                log.setFile(new File(getDataFolder() + File.separator + "logs" + File.separator + getMonth() + File.separator + getDay() + File.separator + filename));
                log.setWriter(new BufferedWriter(new FileWriter(log.getFile(), true)));
                log.setCategory(category);
            } catch (IOException e) {
                logger.severe("Error setting up logger for " + filename + "!");
                logger.severe(e.getMessage());
            }
            loggers.put(filename, log);
            return log;
        } else {
            return loggers.get(filename);
        }
    }


    public Settings getSettings() {
        return this.settings;
    }

    private String getTime() {
        return String.format("[%tm/%<td/%<ty - %<tH:%<tM:%<tS] ", new Date());
    }

    private String getMonth() {
        return String.format("%tb", new Date());
    }

    private String getDay() {
        return String.format("%td", new Date());
    }
}
