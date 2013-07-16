package com.zack6849.superlogger;

import static com.zack6849.superlogger.EventsHandler.debug;
import static com.zack6849.superlogger.EventsHandler.plugin;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {
    //Declaring variables and files

    Logger log;
    public static boolean oldlog;
    public static boolean permissions;
    public static File connections;
    public static File commands;
    public static File chat;
    public static File death;
    public static File logfile;
    public static File[] allfiles = new File[5];
    public static List<String> blocked = new ArrayList<String>();
    public static List<String> whitelist = new ArrayList<String>();
    public static HashMap<File, BufferedWriter> writers = new HashMap<File, BufferedWriter>();
    public static Updater updater;
    //d
    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        try {
            this.log = getLogger();
            //register event handlers
            getServer().getPluginManager().registerEvents(new EventsHandler(this), this);
            //register the different files
            load();
            boolean update = getConfig().getBoolean("auto-update");
            //instanciates the config file object
            File f = new File(getDataFolder(), "config.yml");
            if (!f.exists()) {
                saveDefaultConfig();
            }
            //clearing blocked to be sure nothing gets added multiple times in case of reload
            if (!this.getDescription().getName().equals("SuperLogger")) {
                this.getLogger().info("I'd appreciate it if you didnt change the plugins name, it messes up my plugin metrics. -zack6849");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            blocked.clear();
            for (String s : getConfig().getStringList("filters")) {
                blocked.add(s.toLowerCase());
            }
            whitelist.clear();
            for (String s : getConfig().getStringList("whitelist")) {
                whitelist.add(s.toLowerCase());
            }
            String begin = "########## BEGIN LOGGING AT " + getTime() + "#########";
            if (oldlog) {
                logToFile("===========================================================");
                logToFile(begin);
                logToFile("===========================================================");
                this.log.info("Old logging enabled!");
            } else {
                logToAll("===========================================================");
                logToAll(begin);
                logToAll("===========================================================");
            }
            if (update) {
                main.updater = new Updater(this, "super-logger", this.getFile(), Updater.UpdateType.DEFAULT, true);
            }
            Metrics metrics = new Metrics(this);
            metrics.start();
            this.log.info("Metrics running! <3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        String end = String.format("########## END LOGGING AT " + getTime() + "#########");
        if (oldlog) {
            logToFile("===========================================================");
            logToFile(end);
            logToFile("===========================================================");
        } else {
            logToAll("===========================================================");
            logToAll(end);
            logToAll("===========================================================");
        }
    }
    public void load(){
        main.connections = new File(getDataFolder() + File.separator + getMonth() + File.separator + getDay() + File.separator + "connections.txt");
        main.commands = new File(getDataFolder() + File.separator + getMonth() + File.separator + getDay() + File.separator + "commands.txt");
        main.death = new File(getDataFolder() + File.separator + getMonth() + File.separator + getDay() + File.separator + "deaths.txt");
        main.logfile = new File(getDataFolder() + File.separator + getMonth() + File.separator + getDay() + File.separator + "log.txt");
        main.chat = new File(getDataFolder() + File.separator + getMonth() + File.separator + getDay() + File.separator + "chat.txt");
        main.permissions = this.getConfig().getBoolean("use-permissions");
        oldlog = getConfig().getBoolean("use-old-logging");
        allfiles[0] = connections;
        allfiles[1] = commands;
        allfiles[2] = death;
        allfiles[3] = logfile;
        allfiles[4] = chat;
    }
        
    public void log(File log, String message) {
        try {
            if (!log.exists()) {
                debug("file " + log.getName() + " didn't exist, creating new one");
                log.getParentFile().mkdirs();
                log.createNewFile();
            }
            debug("writing line " + message + " to " + log.getName());
            BufferedWriter br = getBufferedWriter(log);
            br.write(message);
            br.newLine();
            br.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BufferedWriter getBufferedWriter(File f) {
        try {
            if (writers.containsKey(f)) {
                debug("found writer for file " + f.getName());
                return writers.get(f);
            } else {
                debug("Coudlnt find writer for file " + f.getName());
                BufferedWriter returns = new BufferedWriter(new FileWriter(f, true));
                writers.put(f, returns);
                return returns;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void logToAll(String message) {
        try {
            for (File f : main.allfiles) {
                if (!f.exists()) {
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                }
                BufferedWriter br = getBufferedWriter(f);
                br.write(message);
                br.newLine();
                br.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logToFile(String message) {
        try {
            String filename = File.separator + getMonth() + File.separator + getDay() + File.separator + "log.txt";
            File f = new File(getDataFolder() + filename);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
          
            BufferedWriter br = getBufferedWriter(f);
            br.write(message);
            br.newLine();
            br.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sl")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("sl.reload")) {
                        this.reloadConfig();
                        sender.sendMessage(ChatColor.GREEN + "Configuration file reloaded.");
                        blocked.clear();
                        for (String s : getConfig().getStringList("filters")) {
                            blocked.add(s.toLowerCase());
                        }
                        EventsHandler.reload();
                        load();
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Error: you don't have permission to do this!");
                        return true;
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /sl");
                return true;
            }
        }

        return false;
    }

    public static String getTime() {
        return String.format("[%tm/%<td/%<ty - %<tH:%<tM:%<tS] ", new Date());
    }

    public static String getMonth() {
        return String.format("%tb", new Date());
    }

    public static String getDay() {
        return String.format("%td", new Date());
    }
}
