package me.zack6849.logger;

import java.util.Date;

import org.bukkit.Material;
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
import org.bukkit.inventory.ItemStack;

public class EventsHandler implements Listener {
	main plugin;
	public EventsHandler(main main) {
		plugin = main;
	}
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
		boolean command = plugin.getConfig().getBoolean("log-commands");
		if(command){
			if(!e.getPlayer().hasPermission("superlogger.bypass.command")){
				String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
				plugin.logToFile("[COMMAND]" + time + e.getPlayer().getName() + " used command " +  e.getMessage());
				plugin.logCommand("[COMMAND]" + time + e.getPlayer().getName() + " used command " +  e.getMessage());
			}
		}
	}
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		boolean chat = plugin.getConfig().getBoolean("log-chat");
		if(chat){
			if(!e.getPlayer().hasPermission("superlogger.bypass.chat")){
				String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
				plugin.logChat("[CHAT]" + time + e.getPlayer().getName() + " " +  e.getMessage());
				plugin.logToFile("[CHAT]" + time + e.getPlayer().getName() + " " +  e.getMessage());
			}
		}
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		boolean join = plugin.getConfig().getBoolean("log-join");
		if(join){
			if(!e.getPlayer().hasPermission("superlogger.bypass.connection")){
				String ip = e.getPlayer().getAddress().getAddress().toString();
				String ipa = String.format(ip.replaceAll("/", ""));
				String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
				plugin.logJoin("[JOIN]" + time  + e.getPlayer().getName() + " joined the server from ip " + ipa);
				plugin.logToFile("[JOIN]" + time  + e.getPlayer().getName() + " joined the server from ip " + ipa);
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onKick(PlayerKickEvent e){
		boolean kick = plugin.getConfig().getBoolean("log-kick");
		if(kick){
			if(!e.getPlayer().hasPermission("superlogger.bypass.connection")){
				String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
				plugin.logJoin("[KICK]" + time + e.getPlayer().getName() + " was kicked from the server for " + e.getReason());
				plugin.logToFile("[KICK]" + time + e.getPlayer().getName() + " was kicked from the server for " + e.getReason());
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e ){
		boolean quit = plugin.getConfig().getBoolean("log-quit");
		if(quit){
			if(!e.getPlayer().hasPermission("superlogger.bypass.connection")){
				String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
				plugin.logJoin("[LEAVE]" + time + e.getPlayer().getName() + " left the server");
				plugin.logToFile("[LEAVE]" + time + e.getPlayer().getName() + " left the server");
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(PlayerDeathEvent e){
		boolean death = plugin.getConfig().getBoolean("log-death");
		if(death){
			String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
			plugin.logToFile("[DEATH]" + time + e.getDeathMessage());
			plugin.logDeath("[DEATH]" + time + e.getDeathMessage());
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDisaalow(PlayerLoginEvent e){
		boolean disallow = plugin.getConfig().getBoolean("log-disallow");
		if(disallow){
			if(!e.getResult().equals(PlayerLoginEvent.Result.ALLOWED)){
				if(e.getResult().equals(PlayerLoginEvent.Result.KICK_BANNED)){
					String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
					plugin.logToFile("[KICK-BANNED] " + time + e.getPlayer().getName() + " was disconnected from the server because they are banned.");
					plugin.logJoin("[KICK-BANNED] " + time + e.getPlayer().getName() + " was disconnected from the server because they are banned.");
				}
				if(e.getResult().equals(PlayerLoginEvent.Result.KICK_WHITELIST)){
					String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
					plugin.logJoin("[KICK-WHITELIST]" + time + e.getPlayer().getName() + " was disconnected for not being whitelisted.");
					plugin.logToFile("[KICK-WHITELIST]" + time + e.getPlayer().getName() + " was disconnected for not being whitelisted.");
				}
				if(e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)){
					String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
					plugin.logJoin("[KICK-FULL SERVER]" + time + e.getPlayer().getName() + " was disconnected because the server is full.");
					plugin.logToFile("[KICK-FULL SERVER]" + time + e.getPlayer().getName() + " was disconnected because the server is full.");
				}
				if(e.getResult().equals(PlayerLoginEvent.Result.KICK_OTHER)){
					String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
					plugin.logJoin("[KICK-UNKNOWN] " + time + e.getPlayer().getName() + " was disconnected for an unknown reason");
					plugin.logToFile("[KICK-UNKNOWN] " + time + e.getPlayer().getName() + " was disconnected for an unknown reason");
				}
			}
		}
	}
}
