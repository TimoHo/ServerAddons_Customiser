package me.tmods.serveraddons;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.tmods.serverutils.Methods;

public class Customiser extends JavaPlugin implements Listener{
	public File maindataf = new File("plugins/TModsServerUtils","data.yml");
	public FileConfiguration maindata = YamlConfiguration.loadConfiguration(maindataf);
	public FileConfiguration maincfg = YamlConfiguration.loadConfiguration(new File("plugins/TModsServerUtils","config.yml"));
	@Override
	public void onEnable() {
		try {
		if (maindata.getConfigurationSection("CommandAliases") == null) {
			maindata.set("CommandAliases.temporatraousfhglues", "justatemporaryvalue");
			try {
				maindata.save(maindataf);
			} catch (IOException e) {
				e.printStackTrace();
			}
			maindata.set("CommandAliases.temporatraousfhglues", null);
			try {
				maindata.save(maindataf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Bukkit.getPluginManager().registerEvents(this, this);
		} catch (Exception e) {
			Methods.log(e);
		}
	}
	@EventHandler
	public void onCommandProcess(PlayerCommandPreprocessEvent event) {
		try {
		if (maindata.getConfigurationSection("CommandAliases").getKeys(false).contains(event.getMessage().split(" ")[0].replace("/", ""))) {
			event.setCancelled(true);
			String args = "null";
			for (int i = 1;i < event.getMessage().split(" ").length;i++) {
				if (args == "null") {
						args = event.getMessage().split(" ")[i];
				} else {
					if (event.getMessage().split(" ")[i] != "null") {
						args = args + " " + event.getMessage().split(" ")[i];
					}
				}
			}
			if (args == "null" || args == null) {
				event.getPlayer().performCommand(maindata.getString("CommandAliases." + event.getMessage().split(" ")[0].replace("/", "")));
			} else {
				event.getPlayer().performCommand(maindata.getString("CommandAliases." + event.getMessage().split(" ")[0].replace("/", "")) + " " + args);
			}
		}
		} catch (Exception e) {
			Methods.log(e);
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
		if (cmd.getName().equals("aliasCommand")) {
			if (!sender.hasPermission("ServerAddons.customCommand")) {
				sender.sendMessage(Methods.getLang("permdeny"));
				return true;
			}
			if (args.length < 2) {
				return false;
			}
			String cmdargs = null;
			if (args.length > 2) {
				for (int i = 2;i < args.length;i++) {
					if (cmdargs == null) {
						cmdargs = args[i];
					} else {
						cmdargs = cmdargs + " " + args[i];
					}
				}
				maindata.set("CommandAliases." + args[0], args[1] + " " + cmdargs);
			} else {
				maindata.set("CommandAliases." + args[0], args[1]);
			}
			try {
				maindata.save(maindataf);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		} catch (Exception e) {
			Methods.log(e);
		}
		return false;
	}
	
	
}
