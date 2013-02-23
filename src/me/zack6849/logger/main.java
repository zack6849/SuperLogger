package me.zack6849.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {
	Logger log;	
	public static boolean oldlog;
	public static boolean permissions;
	public static File connections;
	public static File commands;
	public static File chat;
	public static File death;
	public static File logfile;
	public static File[] allfiles = new File[5];
	@Override
	public void onEnable(){
		try{
			this.log = getLogger();
			main.permissions = this.getConfig().getBoolean("use-permissions");
			oldlog = getConfig().getBoolean("use-old-logging");
			getServer().getPluginManager().registerEvents(new EventsHandler(this),this);
			boolean update = getConfig().getBoolean("auto-update");
			main.connections = new File(getDataFolder() + File.separator + getMonth() + File.separator + getDay() + File.separator + "connections.txt");
			main.commands = new File(getDataFolder() + File.separator + getMonth() + File.separator + getDay() + File.separator + "commands.txt");
			main.death = new File(getDataFolder() + File.separator + getMonth() + File.separator + getDay() + File.separator + "deaths.txt");
			main.logfile = new File(getDataFolder() + File.separator + getMonth() + File.separator + getDay() + File.separator + "log.txt");
			main.chat = new File(getDataFolder() + File.separator + getMonth() + File.separator + getDay() + File.separator + "chat.txt");
			allfiles[0] = connections;
			allfiles[1] = commands;
			allfiles[2] = death;
			allfiles[3] = logfile;
			allfiles[4] = chat;
			File f = new File(getDataFolder(), "config.yml");
			if(!f.exists()){
				saveDefaultConfig();
			}
			String begin = "########## BEGIN LOGGING AT " + getTime() + "#########";
			if(oldlog){
				logToFile("===========================================================");
				logToFile(begin);
				logToFile("===========================================================");
				this.log.info("Old logging enabled!");
			}else{
				logToAll("===========================================================");
				logToAll(begin);
				logToAll("===========================================================");
			}
			if(update){
				@SuppressWarnings("unused")
				Updater updater = new Updater(this, "super-logger", this.getFile(), Updater.UpdateType.DEFAULT, true);
			}
				Metrics metrics = new Metrics(this);
				metrics.start();
				this.log.info("Metrics running! <3");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void onDisable(){
		String end = String.format("########## END LOGGING AT " + getTime() + "#########");
		if(oldlog){
			logToFile("===========================================================");
			logToFile(end);
			logToFile("===========================================================");
		}else{
			logToAll("===========================================================");
			logToAll(end);
			logToAll("===========================================================");
		}
	}
	public void log(File log,String message){
		try{
			if(!log.exists()){
				log.getParentFile().mkdirs();
				log.createNewFile();
			}
			FileWriter fw = new FileWriter(log, true);
			BufferedWriter br = new BufferedWriter(fw);
			br.write(message);
			br.newLine();
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void logToAll(String message){
		try{
			for(File f : main.allfiles){
				if(!f.exists()){
					f.getParentFile().mkdirs();
					f.createNewFile();
				}
				FileWriter fw = new FileWriter(f, true);
				BufferedWriter br = new BufferedWriter(fw);
				br.write(message);
				br.newLine();
				br.close();
			}
		}catch(Exception e){
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
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter br = new BufferedWriter(fw);
			br.write(message);
			br.newLine();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sl")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("sl.reload")) {
						this.reloadConfig();
						sender.sendMessage(ChatColor.GREEN
								+ "Configuration file reloaded.");
						return true;
					} else {
						sender.sendMessage(ChatColor.RED+ "Error: you don't have permission to do this!");
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
	public static String getTime(){
		return String.format("[%tm/%<td/%<ty - %<tH:%<tM:%<tS] ", new Date());
	}
	public static String getMonth(){
		return String.format("%tb", new Date());
	}
	public static String getDay(){
		return String.format("%td", new Date());
	}
}

