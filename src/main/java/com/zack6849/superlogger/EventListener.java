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
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class EventListener implements Listener {
    private Main plugin;

    public EventListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    /**
     * PlayerChatEvent is 'deprecated' but quite frankly, i'm not going to go find a way to log things aysnc without breaking things, so fuck it.
     */
    public void onChat(PlayerChatEvent event) {
        if (!plugin.getSettings().isLogChat() || event.getPlayer().hasPermission("superlogger.bypass.chat")) {
            plugin.debug("Ignoring " + event.getClass().getSimpleName());
            return;
        }
        LoggingCategory category = plugin.getSettings().isUseOldLogging() ? LoggingCategory.ALL : LoggingCategory.CHAT;
        StringBuilder line = new StringBuilder();
        if (plugin.getSettings().isUseOldLogging()) {
            line.append("[CHAT] ");
        }
        line.append(event.getPlayer().getName()).append(" ");
        if (plugin.getSettings().isLogPlayerUUID()) {
            line.append(String.format("(%s) ", event.getPlayer().getUniqueId().toString()));
        }
        if (plugin.getSettings().isLogPlayerIp()) {
            line.append(String.format("from %s ", event.getPlayer().getAddress().getHostString().replace("/", "")));
        }
        if (plugin.getSettings().isLogCoordinates()) {
            Location loc = event.getPlayer().getLocation();
            line.append(String.format("at (%s, %s, %s) in world '%s' ", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName()));
        }
        line.append(": ").append(event.getMessage());
        plugin.log(line.toString(), category);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        //if the plugin is supposed to be ignoring commands, or the command is filtered, ignore it.
        if (!plugin.getSettings().isLogCommands() || isFiltered(event.getMessage()) || event.getPlayer().hasPermission("superlogger.bypass.command")) {
            plugin.debug("Ignoring " + event.getClass().getSimpleName());
            return;
        }
        //if we're supposed to make sure a command is real, and this one isn't, ignore it.
        if (plugin.getSettings().isCheckCommandExists() && plugin.getServer().getPluginCommand(event.getMessage().split(" ")[0]) == null) {
            plugin.debug("Ignoring " + event.getClass().getSimpleName() + " because the command " + event.getMessage().split(" ")[0] + " wasn't real!");
            return;
        }
        //the relevant methods are literally the exact same as player chat, so copy paste! :D
        LoggingCategory category = plugin.getSettings().isUseOldLogging() ? LoggingCategory.ALL : LoggingCategory.COMMANDS;
        StringBuilder line = new StringBuilder();
        if (plugin.getSettings().isUseOldLogging()) {
            line.append("[COMMAND] ");
        }
        line.append(event.getPlayer().getName()).append(" ");
        if (plugin.getSettings().isLogPlayerUUID()) {
            line.append(String.format("(%s) ", event.getPlayer().getUniqueId().toString()));
        }
        if (plugin.getSettings().isLogPlayerIp()) {
            line.append(String.format("from %s ", event.getPlayer().getAddress().getHostString().replace("/", "")));
        }
        if (plugin.getSettings().isLogCoordinates()) {
            Location loc = event.getPlayer().getLocation();
            line.append(String.format("at (%s, %s, %s) in world '%s' ", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName()));
        }
        line.append(String.format("ran command '%s'", event.getMessage()));
        plugin.log(line.toString(), category);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (plugin.getSettings().isUpdateNotify() && !plugin.getSettings().isAutoUpdate() && plugin.getUpdater().getResult() == Updater.UpdateResult.UPDATE_AVAILABLE && event.getPlayer().hasPermission("superlogger.update.notify")) {
            //for some stupid reason, sending those all at once upon player join causes the messages to come out of order, fuck you bukkit.
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                public void run() {
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "=====================================================");
                    event.getPlayer().sendMessage(ChatColor.GREEN + "An update for SuperLogger is available!");
                    event.getPlayer().sendMessage(ChatColor.GREEN + "New Version: " + plugin.getUpdater().getLatestName());
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Project Page: " + plugin.shortenUrl(plugin.getDescription().getWebsite()));
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Direct Link: " + plugin.shortenUrl(plugin.getUpdater().getLatestFileLink()));
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "To disable this notification, change update-notify to false.");
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "=====================================================");
                }
            }, 10L);
        }

        if (!plugin.getSettings().isLogJoin() || event.getPlayer().hasPermission("superlogger.bypass.connection")) {
            plugin.debug("Ignoring " + event.getClass().getSimpleName());
            return;
        }
        LoggingCategory category = plugin.getSettings().isUseOldLogging() ? LoggingCategory.ALL : LoggingCategory.JOIN;
        StringBuilder line = new StringBuilder();
        if (plugin.getSettings().isUseOldLogging()) {
            line.append("[LOGIN] ");
        }
        line.append("[JOIN] ");
        line.append(event.getPlayer().getName()).append(" ");
        if (plugin.getSettings().isLogPlayerUUID()) {
            line.append(String.format("(%s) ", event.getPlayer().getUniqueId().toString()));
        }
        if (plugin.getSettings().isLogPlayerIp()) {
            line.append(String.format("from %s ", event.getPlayer().getAddress().getHostString().replace("/", "")));
        }

        if (plugin.getSettings().isLogCoordinates()) {
            Location loc = event.getPlayer().getLocation();
            line.append(String.format("at (%s, %s, %s) in world '%s' ", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName()));
        }
        line.append("joined the game!");
        plugin.log(line.toString(), category);
    }

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        //this permission check won't work on certain permission managers if i recall correctly.
        if (!plugin.getSettings().isLogDisallowedConnections() || event.getPlayer().hasPermission("superlogger.bypass.connection")) {
            plugin.debug("Ignoring " + event.getClass().getSimpleName());
            return;
        }
        LoggingCategory category = plugin.getSettings().isUseOldLogging() ? LoggingCategory.ALL : LoggingCategory.FAILED_LOGIN;
        StringBuilder line = new StringBuilder();
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            if (plugin.getSettings().isUseOldLogging()) {
                line.append("[CONNECTION] ");
            }
            line.append("[FAILED LOGIN] ");
            line.append(event.getPlayer().getName()).append(" ");
            if (plugin.getSettings().isLogPlayerUUID()) {
                line.append(String.format("(%s) ", event.getPlayer().getUniqueId().toString()));
            }
            if (plugin.getSettings().isLogPlayerIp()) {
                line.append(String.format("from %s ", event.getAddress().getHostAddress()));
            }
            line.append(String.format("was blocked from joining the game (%s) ", event.getResult().name()));
            line.append("was blocked from joining the game (").append(event.getResult().name()).append(")");
            plugin.log(line.toString(), category);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!plugin.getSettings().isLogQuit() || event.getPlayer().hasPermission("superlogger.bypass.connection")) {
            plugin.debug("Ignoring " + event.getClass().getSimpleName());
            return;
        }
        LoggingCategory category = plugin.getSettings().isUseOldLogging() ? LoggingCategory.ALL : LoggingCategory.QUIT;
        StringBuilder line = new StringBuilder();
        if (plugin.getSettings().isUseOldLogging()) {
            line.append("[CONNECTION] ");
        }
        line.append("[QUIT] ");
        line.append(event.getPlayer().getName()).append(" ");
        if (plugin.getSettings().isLogPlayerUUID()) {
            line.append(String.format("(%s) ", event.getPlayer().getUniqueId().toString()));
        }
        if (plugin.getSettings().isLogPlayerIp()) {
            line.append(String.format("from %s ", event.getPlayer().getAddress().getHostString().replace("/", "")));
        }
        if (plugin.getSettings().isLogCoordinates()) {
            Location loc = event.getPlayer().getLocation();
            line.append(String.format("at (%s, %s, %s) in world '%s' ", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName()));
        }
        line.append("left the the game");
        plugin.log(line.toString(), category);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (!plugin.getSettings().isLogKick() || event.getPlayer().hasPermission("superlogger.bypass.connection")) {
            plugin.debug("Ignoring " + event.getClass().getSimpleName());
            return;
        }
        LoggingCategory category = plugin.getSettings().isUseOldLogging() ? LoggingCategory.ALL : LoggingCategory.KICK;
        StringBuilder line = new StringBuilder();
        if (plugin.getSettings().isUseOldLogging()) {
            line.append("[CONNECTION] ");
        }
        line.append("[KICK] ");
        line.append(event.getPlayer().getName()).append(" ");
        if (plugin.getSettings().isLogPlayerUUID()) {
            line.append(String.format("(%s) ", event.getPlayer().getUniqueId().toString()));
        }
        if (plugin.getSettings().isLogPlayerIp()) {
            line.append(String.format("from %s ", event.getPlayer().getAddress().getHostString().replace("/", "")));
        }
        if (plugin.getSettings().isLogCoordinates()) {
            Location loc = event.getPlayer().getLocation();
            line.append(String.format("at (%s, %s, %s) in world '%s' ", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName()));
        }
        line.append(String.format("was kicked for reason '%s'", event.getReason()));
        plugin.log(line.toString(), category);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!plugin.getSettings().isLogDeath() || event.getEntity().hasPermission("superlogger.bypass.death")) {
            plugin.debug("Ignoring " + event.getClass().getSimpleName());
            return;
        }
        LoggingCategory category = plugin.getSettings().isUseOldLogging() ? LoggingCategory.ALL : LoggingCategory.DEATH;
        StringBuilder line = new StringBuilder();
        if (plugin.getSettings().isUseOldLogging()) {
            line.append("[DEATH] ");
        }
        line.append(event.getEntity().getName()).append(" ");
        if (plugin.getSettings().isLogPlayerUUID()) {
            line.append(String.format("(%s) ", event.getEntity().getUniqueId().toString()));
        }
        if (plugin.getSettings().isLogPlayerIp()) {
            line.append(String.format("from %s ", event.getEntity().getAddress().toString().replace("/", "")));
        }
        if (plugin.getSettings().isLogCoordinates()) {
            Location loc = event.getEntity().getLocation();
            line.append(String.format("at (%s, %s, %s) in world '%s' ", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName()));
        }
        line.append(String.format("died! (%s)", event.getDeathMessage()));
        plugin.log(line.toString(), category);
    }

    public boolean isFiltered(String command) {
        return plugin.getSettings().getFilteredCommands().contains(command.toLowerCase().split(" ")[0].substring(1));
    }
}
