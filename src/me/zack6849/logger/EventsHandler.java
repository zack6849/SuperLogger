package me.zack6849.logger;

import java.util.Date;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventsHandler implements Listener {

	String time = String.format("[%tD - %tT] ", new Date(), new Date());
	main plugin;
	public EventsHandler(main main) {
		plugin = main;
	}
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
	boolean command = plugin.getConfig().getBoolean("log-commands");
		if(command){
			plugin.logToFile("[COMMAND]" + time + e.getPlayer().getName() + " used command " +  e.getMessage());
		}
	}
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		boolean chat = plugin.getConfig().getBoolean("log-chat");
		if(chat){
			plugin.logToFile("[CHAT]" + time + e.getPlayer().getName() + " " +  e.getMessage());
		}
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		boolean join = plugin.getConfig().getBoolean("log-join");
		if(join){
			String ip = e.getPlayer().getAddress().getAddress().toString();
			String ipa = String.format(ip.replaceAll("/", ""));
			plugin.logToFile("[JOIN]" + time  + e.getPlayer().getName() + " joined the server from ip " + ipa);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onKick(PlayerKickEvent e){
		boolean kick = plugin.getConfig().getBoolean("log-kick");
		if(kick){
			plugin.logToFile("[KICK]" + time + e.getPlayer().getName() + " was kicked from the server for " + e.getReason());
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e ){
		boolean quit = plugin.getConfig().getBoolean("log-quit");
		if(quit){
			plugin.logToFile("[LEAVE]" + time + e.getPlayer().getName() + " left the server");
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDisaalow(PlayerLoginEvent e){
		boolean disallow = plugin.getConfig().getBoolean("log-disallow");
		if(disallow){
			if(!e.getResult().equals(PlayerLoginEvent.Result.ALLOWED)){
				if(e.getResult().equals(PlayerLoginEvent.Result.KICK_BANNED)){
					plugin.logToFile("[KICK-BANNED] " + time + e.getPlayer().getName() + " was disconnected from the server because they are banned.");
				}
				if(e.getResult().equals(PlayerLoginEvent.Result.KICK_WHITELIST)){
					plugin.logToFile("[KICK-WHITELIST]" + time + e.getPlayer().getName() + " was disconnected for not being whitelisted.");
				}
				if(e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)){
					plugin.logToFile("[KICK-FULL SERVER]" + time + e.getPlayer().getName() + " was disconnected because the server is full.");
				}
				if(e.getResult().equals(PlayerLoginEvent.Result.KICK_OTHER)){
					plugin.logToFile("[KICK-UNKNOWN] " + e.getPlayer().getName() + " was disconnected for an unknown reason");
				}
			}
		}
	}
}
