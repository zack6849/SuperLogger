package me.zack6849.logger;

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
	public EventsHandler(main main) {
		plugin = main;
	}
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
		boolean command = plugin.getConfig().getBoolean("log-commands");
		if(command){
			if(main.permissions){
				if(!e.getPlayer().hasPermission("superlogger.bypass.command")){
					if(!main.oldlog){
						plugin.log(main.commands , main.getTime() +"[COMMAND] "+ e.getPlayer().getName() + " used command " +  e.getMessage());
					}
					plugin.logToFile(main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " +  e.getMessage());
				}
			}else{
				if(!main.oldlog){
					plugin.log(main.commands , main.getTime() +"[COMMAND] "+ e.getPlayer().getName() + " used command " +  e.getMessage());
				}
				plugin.logToFile(main.getTime() + "[COMMAND] " + e.getPlayer().getName() + " used command " +  e.getMessage());
			}
		}
	}
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		boolean chat = plugin.getConfig().getBoolean("log-chat");
		if(chat){
			if(main.permissions){
				if(!e.getPlayer().hasPermission("superlogger.bypass.chat")){
					if(!main.oldlog){
						plugin.log(main.chat, main.getTime()+ "[CHAT] <" + e.getPlayer().getName() + "> " +  e.getMessage());	
					}
					plugin.logToFile(main.getTime() + "[CHAT] <" + e.getPlayer().getName() + "> " +  e.getMessage());
				}
			}else{
				if(!main.oldlog){
					plugin.log(main.chat, main.getTime()+ "[CHAT] <" + e.getPlayer().getName() + "> " +  e.getMessage());	
				}
				plugin.logToFile(main.getTime() + "[CHAT] <" + e.getPlayer().getName() + "> " +  e.getMessage());
			}
		}
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		boolean join = plugin.getConfig().getBoolean("log-join");
		boolean ipb = plugin.getConfig().getBoolean("log-ip");
		if(join){
			if(main.permissions){
				if(!e.getPlayer().hasPermission("superlogger.bypass.connection")){
					String ip = e.getPlayer().getAddress().getAddress().toString().replaceAll("/", "");
					if(!main.oldlog){
						if(ipb){
							plugin.log(main.connections ,main.getTime() + "[JOIN] "  + e.getPlayer().getName() + " joined the server from ip " + ip);
							plugin.logToFile( main.getTime() +"[JOIN] " +e.getPlayer().getName() + " joined the server from ip " + ip);
						}else{
							plugin.log(main.connections , main.getTime() + "[JOIN] "  + e.getPlayer().getName() + " joined the server from ip " + ip);
							plugin.logToFile(main.getTime() + "[JOIN] "  + e.getPlayer().getName() + " joined the server");
						}
					}else{
						if(ipb){
							plugin.logToFile( main.getTime() + "[JOIN] " +e.getPlayer().getName() + " joined the server from ip " + ip);
						}else{
							plugin.logToFile(main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server");
						}
					}
				}
			}else{
				String ip = e.getPlayer().getAddress().getAddress().toString().replaceAll("/", "");
				if(!main.oldlog){
					if(ipb){
						plugin.log(main.connections ,main.getTime() + "[JOIN] "  + e.getPlayer().getName() + " joined the server from ip " + ip);
						plugin.logToFile( main.getTime() +"[JOIN] " +e.getPlayer().getName() + " joined the server from ip " + ip);
					}else{
						plugin.log(main.connections , main.getTime() + "[JOIN] "  + e.getPlayer().getName() + " joined the server from ip " + ip);
						plugin.logToFile(main.getTime() + "[JOIN] "  + e.getPlayer().getName() + " joined the server");
					}
				}else{
					if(ipb){
						plugin.logToFile( main.getTime() + "[JOIN] " +e.getPlayer().getName() + " joined the server from ip " + ip);
					}else{
						plugin.logToFile(main.getTime() + "[JOIN] " + e.getPlayer().getName() + " joined the server");
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onKick(PlayerKickEvent e){
		boolean kick = plugin.getConfig().getBoolean("log-kick");
		if(kick){
			if(main.permissions){
				if(!e.getPlayer().hasPermission("superlogger.bypass.connection")){
					if(!main.oldlog){
						plugin.log(main.connections,  main.getTime() + "[KICK]" + e.getPlayer().getName() + " was kicked from the server for " + e.getReason());	
					}
					plugin.logToFile(main.getTime() +"[KICK] " +  e.getPlayer().getName() + " was kicked from the server for " + e.getReason());
				}
			}else{
				if(!main.oldlog){
					plugin.log(main.connections,  main.getTime() + "[KICK]" + e.getPlayer().getName() + " was kicked from the server for " + e.getReason());	
				}
				plugin.logToFile(main.getTime() +"[KICK] " +  e.getPlayer().getName() + " was kicked from the server for " + e.getReason());
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e ){
		boolean quit = plugin.getConfig().getBoolean("log-quit");
		if(quit){
			if(main.permissions){
				if(!e.getPlayer().hasPermission("superlogger.bypass.connection")){
					plugin.logToFile(main.getTime() + "[LEAVE] " +  e.getPlayer().getName() + " left the server");
					if(!main.oldlog){
						plugin.log(main.connections, main.getTime() + "[LEAVE] " +  e.getPlayer().getName() + " left the server");
					}
				}
			}else{
				plugin.logToFile(main.getTime() + "[LEAVE] " +  e.getPlayer().getName() + " left the server");
				if(!main.oldlog){
					plugin.log(main.connections, main.getTime() + "[LEAVE] " +  e.getPlayer().getName() + " left the server");
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(PlayerDeathEvent e){
		boolean death = plugin.getConfig().getBoolean("log-death");
		if(death){		
			if(!e.getEntity().hasPermission("superlogger.bypass.death")){
				plugin.logToFile(main.getTime() + "[DEATH] " +  e.getDeathMessage());
				if(!main.oldlog){
					plugin.log(main.death,  main.getTime() +"[DEATH] " +  e.getDeathMessage());	
				}
			}else{
				plugin.logToFile(main.getTime() + "[DEATH] " +  e.getDeathMessage());
				if(!main.oldlog){
					plugin.log(main.death,  main.getTime() +"[DEATH] " +  e.getDeathMessage());	
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDisaalow(PlayerLoginEvent e){
		boolean disallow = plugin.getConfig().getBoolean("log-disallowed-connections");
		if(disallow){
			if(!e.getResult().equals(PlayerLoginEvent.Result.ALLOWED)){
				if(e.getResult().equals(PlayerLoginEvent.Result.KICK_BANNED)){
					plugin.logToFile(main.getTime() + "[KICK-BANNED] " +e.getPlayer().getName() + " was disconnected from the server because they are banned.");
					if(!main.oldlog){
						plugin.log(main.connections, main.getTime() + "[KICK-BANNED] " +  e.getPlayer().getName() + " was disconnected from the server because they are banned.");
					}
				}
				if(e.getResult().equals(PlayerLoginEvent.Result.KICK_WHITELIST)){
					if(!main.oldlog){
						plugin.log(main.connections,main.getTime() +  "[KICK-WHITELIST] " + e.getPlayer().getName() + " was disconnected for not being whitelisted.");
					}
					plugin.logToFile(main.getTime() + "[KICK-WHITELIST] " + e.getPlayer().getName() + " was disconnected for not being whitelisted.");
				}
				if(e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)){
					if(!main.oldlog){
						plugin.log(main.connections, main.getTime() + "[KICK-FULL SERVER] " + e.getPlayer().getName() + " was disconnected because the server is full.");
					}
					plugin.logToFile(main.getTime() + "[KICK-FULL SERVER] " + e.getPlayer().getName() + " was disconnected because the server is full.");
				}
				if(e.getResult().equals(PlayerLoginEvent.Result.KICK_OTHER)){

					if(!main.oldlog){
						plugin.log(main.connections,  main.getTime() + "[KICK-UNKNOWN] "  + e.getPlayer().getName() + " was disconnected for an unknown reason");	
					}
					plugin.logToFile( main.getTime() +"[KICK-UNKNOWN] " + e.getPlayer().getName() + " was disconnected for an unknown reason");
				}
			}
		}
	}
}
