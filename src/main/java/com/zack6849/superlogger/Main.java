/*
* This file is part of SuperLogger.
*
* SuperLogger is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/

package com.zack6849.superlogger;

import net.gravitydevelopment.updater.Updater;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    private Logger logger;
    private Settings settings;
    private ConcurrentHashMap<String, LoggerAbstraction> loggers;

    @Override
    public void onEnable() {
        this.logger = getLogger();
        this.loggers = new ConcurrentHashMap<String, LoggerAbstraction>();
        saveDefaultConfig();
        loadSettings();
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        try {
            Metrics metrics = new Metrics(this);
            logger.log(Level.INFO, "Metrics Running <3");
        } catch (IOException e) {
            logger.log(Level.WARNING, "There was an issue starting plugin metrics </3");
            logger.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }
        Updater updater = new Updater(this, 45448, this.getFile(), Updater.UpdateType.DEFAULT, true);
    }

    public void loadSettings() {
        if (this.getConfig().getConfigurationSection("log") == null) {
            logger.log(Level.SEVERE, "Your configuration file is out of date, please generate a new one!");
            logger.log(Level.INFO, "Disabling SuperLogger...");
            getPluginLoader().disablePlugin(this);
        }
        this.settings = new Settings();
        settings.setAutoUpdate(getConfig().getBoolean("auto-update"));
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
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /sl <version, reload>");
                return true;
            }
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("version")) {
                    sender.sendMessage(ChatColor.YELLOW + "SuperLogger Version " + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors().toString().replace("[", "").replace("]", ""));
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!sender.hasPermission("sl.reload")) {
                        sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
                        return true;
                    }
                    this.reloadConfig();
                    loadSettings();
                    sender.sendMessage(ChatColor.GREEN + "Configuration file reloaded!");
                    return true;
                }
            }
            //i dont even think this would ever be called, but ok.
            sender.sendMessage(ChatColor.RED + "Invalid syntax! /sl <version, reload>");
            return true;
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
