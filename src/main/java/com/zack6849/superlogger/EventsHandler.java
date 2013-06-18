package com.zack6849.superlogger;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventsHandler implements Listener {
    
    public static main plugin;
    public static boolean debug;
    public static boolean LOG_COMMANDS;
    public static boolean LOG_JOIN;
    public static boolean LOG_CHAT;
    public static boolean LOG_JOIN_IP;
    public static boolean COMMAND_WHITELIST;
    public static boolean LOG_KICK;
    public static boolean LOG_QUIT ;
    public static boolean LOG_DEATH;
    public static boolean LOG_DEATH_LOCATION;
    public static boolean LOG_DISALLOWED_CONNECTIONS;
    public EventsHandler(main main) {
        plugin = main;
        debug = false;
        LOG_COMMANDS = plugin.getConfig().getBoolean("log-commands");
        LOG_JOIN = plugin.getConfig().getBoolean("log-join");
        LOG_CHAT = plugin.getConfig().getBoolean("log-chat");
        LOG_JOIN_IP = plugin.getConfig().getBoolean("log-ip");
        COMMAND_WHITELIST = plugin.getConfig().getBoolean("use-command-whitelist");
        LOG_KICK = plugin.getConfig().getBoolean("log-kick");
        LOG_QUIT = plugin.getConfig().getBoolean("log-quit");
        LOG_DEATH = plugin.getConfig().getBoolean("log-death");
        LOG_DEATH_LOCATION = plugin.getConfig().getBoolean("log-death-location");
        LOG_DISALLOWED_CONNECTIONS = plugin.getConfig().getBoolean("log-disallowed-connections");
    }
    public static void reload(){
        LOG_COMMANDS = plugin.getConfig().getBoolean("log-commands");
        LOG_JOIN = plugin.getConfig().getBoolean("log-join");
        LOG_CHAT = plugin.getConfig().getBoolean("log-chat");
        LOG_JOIN_IP = plugin.getConfig().getBoolean("log-ip");
        COMMAND_WHITELIST = plugin.getConfig().getBoolean("use-command-whitelist");
        LOG_KICK = plugin.getConfig().getBoolean("log-kick");
        LOG_QUIT = plugin.getConfig().getBoolean("log-quit");
        LOG_DEATH = plugin.getConfig().getBoolean("log-death");
        LOG_DEATH_LOCATION = plugin.getConfig().getBoolean("log-death-location");
        LOG_DISALLOWED_CONNECTIONS = plugin.getConfig().getBoolean("log-disallowed-connections");
    }
    
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        debug("command event");
        if (LOG_COMMANDS && ((main.permissions && !e.getPlayer().hasPermission("superlogger.bypass.command")) || !main.permissions)) {
            debug("logging commands");
            String command = e.getMessage().split(" ")[0].replaceFirst("/", "");
            if(plugin.getServer().getPluginCommand(command) == null && !plugin.getConfig().getBoolean("check-commands")){
                return;
            }
            if (COMMAND_WHITELIST && isWhitelisted(e.getMessage())) {
                debug("command whitelisting enabled and command is whitelisted");
                if (!main.oldlog) {
                    debug("old logging disabled");
                    plugin.log(main.commands, main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " + e.getMessage());
                }
                debug("logging to log.txt");
                plugin.logToFile(main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " + e.getMessage());
                return;
            }
            debug("whitelist wasn't enabled.");
            if (!main.oldlog) {
                debug("old logging wasnt enabled");
                plugin.log(main.commands, main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " + e.getMessage());
            }
            debug("logging to main file");
            plugin.logToFile(main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " + e.getMessage());
        }
    }
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (LOG_CHAT) {
            if (main.permissions && !e.getPlayer().hasPermission("superlogger.bypass.chat")) {
                if (!main.oldlog) {
                    plugin.log(main.chat, main.getTime() + "[CHAT] <" + e.getPlayer().getName() + "> " + e.getMessage());
                }
                plugin.logToFile(main.getTime() + "[CHAT] <" + e.getPlayer().getName() + "> " + e.getMessage());
                return;
            }
            if (!main.oldlog) {
                plugin.log(main.chat, main.getTime() + "[CHAT] <" + e.getPlayer().getName() + "> " + e.getMessage());
            }
            plugin.logToFile(main.getTime() + "[CHAT] <" + e.getPlayer().getName() + "> " + e.getMessage());
            return;
        }
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (LOG_JOIN) {
            if (main.permissions && !e.getPlayer().hasPermission("superlogger.bypass.connection")) {
                String log = main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server";
                if (LOG_JOIN_IP) {
                    log += " from ip " + e.getPlayer().getAddress().toString().replaceFirst("/", "");
                }
                if (main.oldlog) {
                    //log to the main
                    plugin.logToFile(log);
                }
                plugin.logToAll(log);
                return;
            }
            String log = main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server";
            if (LOG_JOIN_IP) {
                log += " from ip " + e.getPlayer().getAddress().toString().replaceFirst("/", "");
            }
            if (main.oldlog) {
                plugin.logToFile(log);
            }
            plugin.log(main.connections, log);
        }
    }
    
    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (LOG_KICK) {
            if (main.permissions && !e.getPlayer().hasPermission("superlogger.bypass.connection")) {
                if (!main.oldlog) {
                    plugin.log(main.connections, main.getTime() + "[KICK]" + e.getPlayer().getName() + " was kicked from the server for " + e.getReason());
                }
                plugin.logToFile(main.getTime() + "[KICK] " + e.getPlayer().getName() + " was kicked from the server for " + e.getReason());
                return;
            }
            if (!main.oldlog) {
                plugin.log(main.connections, main.getTime() + "[KICK]" + e.getPlayer().getName() + " was kicked from the server for " + e.getReason());
            }
            plugin.logToFile(main.getTime() + "[KICK] " + e.getPlayer().getName() + " was kicked from the server for " + e.getReason());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (LOG_QUIT) {
            if (main.permissions && !e.getPlayer().hasPermission("superlogger.bypass.connection")) {
                plugin.logToFile(main.getTime() + "[LEAVE] " + e.getPlayer().getName() + " left the server");
                if (!main.oldlog) {
                    plugin.log(main.connections, main.getTime() + "[LEAVE] " + e.getPlayer().getName() + " left the server");
                }
                plugin.log(main.connections, main.getTime() + "[LEAVE] " + e.getPlayer().getName() + " left the server");
                return;
            }
            plugin.logToFile(main.getTime() + "[LEAVE] " + e.getPlayer().getName() + " left the server");
            if (!main.oldlog) {
                plugin.log(main.connections, main.getTime() + "[LEAVE] " + e.getPlayer().getName() + " left the server");
            }
            plugin.log(main.connections, main.getTime() + "[LEAVE] " + e.getPlayer().getName() + " left the server");
        }
    }
    
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        String info = main.getTime() + "[DEATH] " + e.getDeathMessage();
        if (LOG_DEATH) {
            if (main.permissions && !e.getEntity().hasPermission("superlogger.bypass.death")) {
                if (LOG_DEATH_LOCATION) {
                    info += String.format(" at (%s,%s,%s) in world %s", e.getEntity().getLocation().getBlockX(), e.getEntity().getLocation().getBlockY(), e.getEntity().getLocation().getBlockZ(), e.getEntity().getWorld().getName());
                }
                if (main.oldlog) {
                    plugin.log(main.death, info);
                }
                plugin.logToFile(info);
                return;
            }
             if (LOG_DEATH_LOCATION) {
                    info += String.format(" at (%s,%s,%s) in world %s", e.getEntity().getLocation().getBlockX(), e.getEntity().getLocation().getBlockY(), e.getEntity().getLocation().getBlockZ(), e.getEntity().getWorld().getName());
                }
                if (main.oldlog) {
                    plugin.log(main.death, info);
                }
                plugin.logToFile(info);
        }
    }
    
    @EventHandler
    public void onDisalow(PlayerLoginEvent e) {
        if (LOG_DISALLOWED_CONNECTIONS) {
            if (!e.getResult().equals(PlayerLoginEvent.Result.ALLOWED)) {
                if (e.getResult().equals(PlayerLoginEvent.Result.KICK_BANNED)) {
                    plugin.logToFile(main.getTime() + "[KICK-BANNED] " + e.getPlayer().getName() + " was disconnected from the server because they are banned.");
                    if (!main.oldlog) {
                        plugin.log(main.connections, main.getTime() + "[KICK-BANNED] " + e.getPlayer().getName() + " was disconnected from the server because they are banned.");
                    }
                }
                if (e.getResult().equals(PlayerLoginEvent.Result.KICK_WHITELIST)) {
                    if (!main.oldlog) {
                        plugin.log(main.connections, main.getTime() + "[KICK-WHITELIST] " + e.getPlayer().getName() + " was disconnected for not being whitelisted.");
                    }
                    plugin.logToFile(main.getTime() + "[KICK-WHITELIST] " + e.getPlayer().getName() + " was disconnected for not being whitelisted.");
                   
                }
                if (e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) {
                    if (!main.oldlog) {
                        plugin.log(main.connections, main.getTime() + "[KICK-FULL SERVER] " + e.getPlayer().getName() + " was disconnected because the server is full.");
                    }
                    plugin.logToFile(main.getTime() + "[KICK-FULL SERVER] " + e.getPlayer().getName() + " was disconnected because the server is full.");
                }
                if (e.getResult().equals(PlayerLoginEvent.Result.KICK_OTHER)) {
                    if (!main.oldlog) {
                        plugin.log(main.connections, main.getTime() + "[KICK-UNKNOWN] " + e.getPlayer().getName() + " was disconnected for an unknown reason");
                    }
                    plugin.logToFile(main.getTime() + "[KICK-UNKNOWN] " + e.getPlayer().getName() + " was disconnected for an unknown reason");
                }
            }
        }
    }
    
    public boolean isFiltered(String s) {
        boolean flag = false;
        String msg = s.split(" ")[0].toLowerCase().replaceFirst("/", "");
        for (String s1 : main.blocked) {
            if (msg.equalsIgnoreCase(s1)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    
    public boolean isWhitelisted(String s) {
        boolean flag = false;
        String msg = s.split(" ")[0].toLowerCase().replaceFirst("/", "");
        for (String s1 : main.whitelist) {
            if (msg.equalsIgnoreCase(s1)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    public static void debug(String s) {
        if (debug) {
            plugin.log.log(Level.FINEST, s);
            Bukkit.broadcastMessage("[SUPERLOGGER] DEBUG: " + s);
        }
    }
}
