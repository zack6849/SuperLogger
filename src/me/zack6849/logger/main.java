package me.zack6849.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {
	Logger log;	
	public void onEnable(){
		this.log = getLogger();
		getServer().getPluginManager().registerEvents(new EventsHandler(this),this);
		boolean update = getConfig().getBoolean("auto-update");
		saveResource("log.txt", false);
		String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
		logToFile("########## BEGIN LOGGING AT " + time + "#########");
		File f = new File(getDataFolder(), "config.yml");
		if(!f.exists()){
			saveDefaultConfig();
		}
		if(update){
				Updater updater = new Updater(this, "super-logger", this.getFile(), Updater.UpdateType.DEFAULT, true);
			}
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			this.log.info("Failed to send stats for metrics!");
		}
	}
	public void onDisable(){
		String time = String.format("[%tm/%td/%ty - %tH:%tM:%tS] ", new Date(), new Date(),new Date(),new Date(),new Date(),new Date());
		String end = String.format("########## END LOGGING AT " + time + "#########");
		logToFile(end);
		logToFile("");
		logToFile("");
		logToFile("");
	}
	public void logToFile(String message)
	{
			try
			{
				File f = new File(getDataFolder(), "log.txt");
				FileWriter fw = new FileWriter(f, true);

				PrintWriter pw = new PrintWriter(fw);

				pw.println(message);

				pw.flush();

				pw.close();

			} catch (IOException e)
			{

				e.printStackTrace();

			}

		}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("sl")){
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("reload")){
					if(sender.hasPermission("sl.reload")){
						this.reloadConfig();
						sender.sendMessage(ChatColor.GREEN + "Configuration file reloaded.");	
						return true;
					}else{
						sender.sendMessage(ChatColor.RED + "Error: you don't have permission to do this!");
						return true;
					}
				}
			}else{
				sender.sendMessage(ChatColor.RED + "Usage: /sl");
				return true;
			}
		}
		return false;
	}
}	
