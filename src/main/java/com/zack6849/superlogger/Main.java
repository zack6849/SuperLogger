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

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    private Logger logger;
    private Settings settings;
    private ConcurrentHashMap<String, LoggerAbstraction> loggers;
    //private Updater updater;

    @Override
    public void onEnable() {
        this.logger = getLogger();
        this.loggers = new ConcurrentHashMap<String, LoggerAbstraction>();
        saveDefaultConfig();
        getConfig().setDefaults(new MemoryConfiguration());
        loadSettings();
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        /*updater = new Updater(this, getFile(), "super-logger");
        updater.fetchData();
        logger.info("Starting updater and checking for updates");
        if(settings.isAutoUpdate() && updater.isUpdateAvailible()){
            try {
                logger.info("update availible, downloading.");
                updater.updatePlugin();
                logger.info("update complete");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            logger.info("No update availible!");
        }*/

        if (settings.isDebug()) {
            for (String line : getDebug()) {
                debug(line);
            }
        }
    }

    public void loadSettings() {
        if (!getConfig().isConfigurationSection("log") | this.getConfig().getConfigurationSection("log") == null) {
            //TODO: Clean this code up somehow?
            logger.info("Doing one-time config conversion");
            getConfig().set("auto-update", getConfig().getBoolean("auto-update"));
            getConfig().set("update-notify", true);
            getConfig().set("debug", false);
            getConfig().set("log.commands", getConfig().getBoolean("log-commands"));
            getConfig().set("log.check-command-exists", getConfig().getBoolean("check-commands"));
            getConfig().set("log.chat", getConfig().getBoolean("log-chat"));
            getConfig().set("log.join", getConfig().getBoolean("log-join"));
            getConfig().set("log.quit", getConfig().getBoolean("log-quit"));
            getConfig().set("log.kick", getConfig().getBoolean("log-kick"));
            getConfig().set("log.death", getConfig().getBoolean("log-death"));
            getConfig().set("log.failed-connections", getConfig().getBoolean("log-disallowed-connections"));
            getConfig().set("log.player-ip", getConfig().getBoolean("log-ip"));
            getConfig().set("log.player-uuid", true);
            getConfig().set("log.coordinates", true);
            getConfig().set("blacklist", getConfig().getStringList("filters"));

            //remove old config nodes
            List<String> oldpaths = Arrays.asList("log-commands log-chat log-join log-quit log-kick log-death log-ip log-death-location log-disallowed-connections use-old-logging use-permissions notify-update use-command-whitelist check-commands whitelist filters".split(" "));
            for (String path : oldpaths) {
                getConfig().set(path, null);
            }
            //save and reload config
            try {
                getConfig().save(new File(getDataFolder(), "config.yml"));
                reloadConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.settings = new Settings();
        settings.setDebug(getConfig().getBoolean("debug", true));
        settings.setAutoUpdate(getConfig().getBoolean("auto-update", true));
        settings.setUpdateNotify(getConfig().getBoolean("update-notify", true));
        settings.setLogChat(getConfig().getBoolean("log.chat", true));
        settings.setLogCommands(getConfig().getBoolean("log.commands", true));
        settings.setLogCoordinates(getConfig().getBoolean("log.coordinates", true));
        settings.setLogDeath(getConfig().getBoolean("log.death", true));
        settings.setLogJoin(getConfig().getBoolean("log.join", true));
        settings.setLogQuit(getConfig().getBoolean("log.quit", true));
        settings.setLogKick(getConfig().getBoolean("log.kick", true));
        settings.setLogDisallowedConnections(getConfig().getBoolean("log.failed-connections", true));
        settings.setLogPlayerIp(getConfig().getBoolean("log.player-ip", true));
        settings.setLogPlayerUUID(getConfig().getBoolean("log.player-uuid", true));
        settings.setCheckCommandExists(getConfig().getBoolean("log.check-command-exists", false));
        settings.setFilteredCommands(new HashSet<String>());
        for (String command : getConfig().getStringList("blacklist")) {
            //lowercase all commands so it's easier to check
            settings.getFilteredCommands().add(StringUtils.deleteWhitespace(command));
        }
        if (getSettings().isLogPlayerUUID()) {
            debug("UUID support is enabled, checking to see if server supports UUIDs");
            double version = Double.valueOf(StringUtils.join(getServer().getVersion().split("\\(")[1].split("\\)")[0].split("MC: ")[1].split("\\."), ".", 0, 2));
            debug("Found minecraft version " + version + " from version string " + getServer().getVersion());
            //actual UUID support is only in minecraft 1.7+
            if (version < 1.7) {
                logger.warning("Player UUID Logging is enabled, but your server version doesn't have it!");
                logger.warning("Disabling UUID Logging..");
                getSettings().setLogPlayerUUID(false);
            }
        }
        /*if(getSettings().isUpdateNotify() || getSettings().isAutoUpdate()){
            //refresh data every 5 minutes.
            getServer().getScheduler().runTaskTimer(this, new Runnable() {
                @Override
                public void run() {
                    updater.fetchData();
                }
            },0,  5 * 60 * 1000);
        }*/
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
                    if (!sender.hasPermission("superlogger.reload")) {
                        sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
                        return true;
                    }
                    this.reloadConfig();
                    loadSettings();
                    sender.sendMessage(ChatColor.GREEN + "Configuration file reloaded!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("debug")) {
                    if (sender.hasPermission("superlogger.debug"))
                        sender.sendMessage(ChatColor.GREEN + "Dumping debug information to server log");
                    for (String line : getDebug()) {
                        logger.info(line);
                    }
                    return true;
                }
            }
            //i dont even think this would ever be called, but ok.
            sender.sendMessage(ChatColor.RED + "Invalid syntax! /sl <version, reload, debug>");
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
            logger.severe("Error writing line to " + category.getFileName() + " for LoggingCategory." + category.toString());
            e.printStackTrace();
        }
    }

    public int getCurrentDay(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }


    public LoggerAbstraction getFile(LoggingCategory category) {
        String filename = category.getFileName();
        if (!loggers.containsKey(filename) || (loggers.containsKey(filename) && loggers.get(filename).getDay() != getCurrentDay())) {
            getLogger().info("Creating new LoggerAbstraction for LoggingCategory." + category.toString());
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

    public void debug(String message) {
        if (settings.isDebug()) {
            logger.info("[DEBUG] " + message);
        }
    }

    public List<String> getDebug() {
        List<String> lines = new ArrayList<String>();
        lines.add(String.format("Running version %s with server version %s (%s) on %s %s %s", getDescription().getVersion(), getServer().getVersion(), getServer().getBukkitVersion(), System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch")));
        lines.add("Settings:");
        lines.add("Debug: " + settings.isDebug());
        lines.add("Auto-Update: " + settings.isAutoUpdate());
        lines.add("Update notify: " + settings.isUpdateNotify());
        lines.add("Logging:");
        lines.add("\tLog Event  Co-Ordinates: " + settings.isLogCoordinates());
        lines.add("\tLog Player IP: " + settings.isLogPlayerIp());
        lines.add("\tLog Player UUID: " + settings.isLogPlayerUUID());
        lines.add("\tCheck if commands are real: " + settings.isCheckCommandExists());
        lines.add("\tLog Chat: " + settings.isLogChat());
        lines.add("\tLog Commands: " + settings.isLogCommands());
        lines.add("\tLog Deaths: " + settings.isLogDeath());
        lines.add("\tLog Joins: " + settings.isLogJoin());
        lines.add("\tLog Quits: " + settings.isLogQuit());
        lines.add("\tLog Kicks: " + settings.isLogKick());
        lines.add("\tLog Disallowed Connections: " + settings.isLogDisallowedConnections());
        lines.add("\tFiltered commands: ");
        for (String filter : settings.getFilteredCommands()) {
            lines.add("\t   - " + filter);
        }
        return lines;
    }

   /* public Updater getUpdater() {
        return updater;
    }*/

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

    public String shortenUrl(String longUrl) {
        String shortened = null;
        try {
            URL url;
            url = new URL("http://is.gd/create.php?format=simple&url=" + longUrl);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
            shortened = bufferedreader.readLine();
            bufferedreader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shortened;
    }
}
