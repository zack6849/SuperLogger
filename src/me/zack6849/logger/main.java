package me.zack6849.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {
	Logger log;	
	public static boolean oldlog;
	public void onEnable(){
		this.log = getLogger();
		oldlog = getConfig().getBoolean("old-logging");
		getServer().getPluginManager().registerEvents(new EventsHandler(this),this);
		boolean update = getConfig().getBoolean("auto-update");
		String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
		File f = new File(getDataFolder(), "config.yml");
		String begin = "########## BEGIN LOGGING AT " + time + "#########";
		if(oldlog){
			logToFile(begin);
			this.log.info("Old logging enabled!");
		}else{
			logToFile(begin);
			logDeath(begin);
			logCommand(begin);
			logJoin(begin);
			logChat(begin);	
		}
		if(!f.exists()){
			saveDefaultConfig();
		}
		if(update){
			Updater updater = new Updater(this, "super-logger", this.getFile(), Updater.UpdateType.DEFAULT, true);
		}
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
			this.log.info("Metrics running! <3");
		} catch (IOException e) {
			this.log.info("Failed to send stats for metrics!");
		}
	}
	public void onDisable(){
		String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
		String end = String.format("########## END LOGGING AT " + time + "#########");
		if(oldlog){
			logToFile(end);
			logToFile("");
			logToFile("");
			logToFile("");
		}else{
			logToFile(end);
			logToFile("");
			logToFile("");
			logToFile("");
			logDeath(end);
			logDeath("");
			logDeath("");
			logDeath("");
			logCommand(end);
			logCommand("");
			logCommand("");
			logCommand("");
			logJoin(end);
			logJoin("");
			logJoin("");
			logJoin("");	
		}
	}

	public void logToFile(String message) {
		try {
			String logname = "log.txt";
			String month = String.format("%tb", new Date());
			String day = String.format("%td", new Date());
			File f = new File(getDataFolder() + File.separator + month
					+ File.separator + day + File.separator, logname);
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

	public void logDeath(String message) {
			try {
				String logname = "deaths.txt";
				String month = String.format("%tb", new Date());
				String day = String.format("%td", new Date());
				File f = new File(getDataFolder() + File.separator + month
						+ File.separator + day + File.separator, logname);
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

	public void logCommand(String message) {
			try {
				String logname = "commands.txt";
				String month = String.format("%tb", new Date());
				String day = String.format("%td", new Date());
				File f = new File(getDataFolder() + File.separator + month
						+ File.separator + day + File.separator, logname);
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

	public void logJoin(String message) {
			try {
				String logname = "connections.txt";
				String month = String.format("%tb", new Date());
				String day = String.format("%td", new Date());
				File f = new File(getDataFolder() + File.separator + month
						+ File.separator + day + File.separator, logname);
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

	public void logChat(String message) {
			try {
				String logname = "chat.txt";
				String month = String.format("%tb", new Date());
				String day = String.format("%td", new Date());
				File f = new File(getDataFolder() + File.separator + month
						+ File.separator + day + File.separator, logname);
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

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sl")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("sl.reload")) {
						this.reloadConfig();
						sender.sendMessage(ChatColor.GREEN
								+ "Configuration file reloaded.");
						return true;
					} else {
						sender.sendMessage(ChatColor.RED
								+ "Error: you don't have permission to do this!");
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
}

