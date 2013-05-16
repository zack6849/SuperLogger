package com.zack6849.superlogger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventsHandler implements Listener {

    main plugin;
    public static boolean debug = true;

    public EventsHandler(main main) {
        plugin = main;
    }
    //a
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        boolean command = plugin.getConfig().getBoolean("log-commands");
        boolean whitelist = plugin.getConfig().getBoolean("use-command-whitelist");
        if (command) {
            if (!isFiltered(e.getMessage())) {
                if (main.permissions) {
                    if (!e.getPlayer().hasPermission("superlogger.bypass.command")) {
                        if (whitelist) {
                            if (isWhitelisted(e.getMessage())) {
                                if (!main.oldlog) {
                                    plugin.log(main.commands, main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " + e.getMessage());
                                }
                                plugin.logToFile(main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " + e.getMessage());
                            }
                        } else {
                            if (!main.oldlog) {
                                plugin.log(main.commands, main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " + e.getMessage());
                            }
                            plugin.logToFile(main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " + e.getMessage());
                        }
                    }
                } else {
                    if (whitelist) {
                        if (isWhitelisted(e.getMessage())) {
                            if (!main.oldlog) {
                                plugin.log(main.commands, main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " + e.getMessage());
                            }
                            plugin.logToFile(main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " + e.getMessage());
                        }
                    } else {
                        if (!main.oldlog) {
                            plugin.log(main.commands, main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " + e.getMessage());
                        }
                        plugin.logToFile(main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " + e.getMessage());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        boolean chat = plugin.getConfig().getBoolean("log-chat");
        if (chat) {
            if (main.permissions) {
                if (!e.getPlayer().hasPermission("superlogger.bypass.chat")) {
                    if (!main.oldlog) {
                        plugin.log(main.chat, main.getTime() + "[CHAT] <" + e.getPlayer().getName() + "> " + e.getMessage());
                    }
                    plugin.logToFile(main.getTime() + "[CHAT] <" + e.getPlayer().getName() + "> " + e.getMessage());
                }
            } else {
                if (!main.oldlog) {
                    plugin.log(main.chat, main.getTime() + "[CHAT] <" + e.getPlayer().getName() + "> " + e.getMessage());
                }
                plugin.logToFile(main.getTime() + "[CHAT] <" + e.getPlayer().getName() + "> " + e.getMessage());
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        boolean join = plugin.getConfig().getBoolean("log-join");
        boolean ipb = plugin.getConfig().getBoolean("log-ip");
        /*	UpdateResult res = plugin.updater.getResult();
         if(plugin.getConfig().getBoolean("notify-update")){
         if(res != null && res == UpdateResult.UPDATE_AVAILABLE){
         if(e.getPlayer().isOp() || e.getPlayer().hasPermission("superlogger.update.notify")){
         e.getPlayer().sendMessage(ChatColor.GREEN + "[" + ChatColor.GOLD + "SuperLogger" + ChatColor.YELLOW + "]" + ChatColor.GREEN + " An update is available for LockDown!");
         e.getPlayer().sendMessage(ChatColor.GREEN + "New version: " + ChatColor.GREEN + main.updater.getLatestVersionString());
         e.getPlayer().sendMessage(ChatColor.GREEN + "Current version: " + ChatColor.GREEN + plugin.getDescription().getVersion());
         }
         }
         }*/
        if (join) {
            if (main.permissions) {
                if (!e.getPlayer().hasPermission("superlogger.bypass.connection")) {
                    String ip = e.getPlayer().getAddress().getAddress().toString().replaceAll("/", "");
                    if (!main.oldlog) {
                        if (ipb) {
                            plugin.log(main.connections, main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server from ip " + ip);
                            plugin.logToFile(main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server from ip " + ip);
                        } else {
                            plugin.log(main.connections, main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server from ip " + ip);
                            plugin.logToFile(main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server");
                        }
                    } else {
                        if (ipb) {
                            plugin.logToFile(main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server from ip " + ip);
                        } else {
                            plugin.logToFile(main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server");
                        }
                    }
                }
            } else {
                String ip = e.getPlayer().getAddress().getAddress().toString().replaceAll("/", "");
                if (!main.oldlog) {
                    if (ipb) {
                        plugin.log(main.connections, main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server from ip " + ip);
                        plugin.logToFile(main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server from ip " + ip);
                    } else {
                        plugin.log(main.connections, main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server from ip " + ip);
                        plugin.logToFile(main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server");
                    }
                } else {
                    if (ipb) {
                        plugin.logToFile(main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server from ip " + ip);
                    } else {
                        plugin.logToFile(main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server");
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKick(PlayerKickEvent e) {
        boolean kick = plugin.getConfig().getBoolean("log-kick");
        if (kick) {
            if (main.permissions) {
                if (!e.getPlayer().hasPermission("superlogger.bypass.connection")) {
                    if (!main.oldlog) {
                        plugin.log(main.connections, main.getTime() + "[KICK]" + e.getPlayer().getName() + " was kicked from the server for " + e.getReason());
                    }
                    plugin.logToFile(main.getTime() + "[KICK] " + e.getPlayer().getName() + " was kicked from the server for " + e.getReason());
                }
            } else {
                if (!main.oldlog) {
                    plugin.log(main.connections, main.getTime() + "[KICK]" + e.getPlayer().getName() + " was kicked from the server for " + e.getReason());
                }
                plugin.logToFile(main.getTime() + "[KICK] " + e.getPlayer().getName() + " was kicked from the server for " + e.getReason());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        boolean quit = plugin.getConfig().getBoolean("log-quit");
        if (quit) {
            if (main.permissions) {
                if (!e.getPlayer().hasPermission("superlogger.bypass.connection")) {
                    plugin.logToFile(main.getTime() + "[LEAVE] " + e.getPlayer().getName() + " left the server");
                    if (!main.oldlog) {
                        plugin.log(main.connections, main.getTime() + "[LEAVE] " + e.getPlayer().getName() + " left the server");
                    }
                }
            } else {
                plugin.logToFile(main.getTime() + "[LEAVE] " + e.getPlayer().getName() + " left the server");
                if (!main.oldlog) {
                    plugin.log(main.connections, main.getTime() + "[LEAVE] " + e.getPlayer().getName() + " left the server");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        boolean death = plugin.getConfig().getBoolean("log-death");
        boolean location = plugin.getConfig().getBoolean("log-death-location");
        String info = main.getTime() + "[DEATH] " + e.getDeathMessage();
        if (death) {
            if (!e.getEntity().hasPermission("superlogger.bypass.death")) {
                if (location) {
                    info += " at (" + e.getEntity().getLocation().getBlockX() + ", " + e.getEntity().getLocation().getBlockY() + ", " + e.getEntity().getLocation().getBlockZ() + ") in world " + e.getEntity().getWorld().getName();
                }
                if (!main.oldlog) {
                    plugin.log(main.death, info);
                }
                plugin.logToFile(info);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDisalow(PlayerLoginEvent e) {
        boolean disallow = plugin.getConfig().getBoolean("log-disallowed-connections");
        if (disallow) {
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
}
