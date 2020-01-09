package com.devlucca.plotgui;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.devlucca.plotgui.command.Terreno;
import com.devlucca.plotgui.constructors.Corretor;
import com.devlucca.plotgui.constructors.Pedreiro;
import com.devlucca.plotgui.data.FileManager;
import com.devlucca.plotgui.events.Events;
import com.devlucca.plotgui.usefull.Formater;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	private static Plugin plugin;

	public static String plotworldname = "";
	public static ArrayList<Pedreiro> ped;

	public static Economy economy;

	static {
		Main.economy = null;
	}

	@SuppressWarnings("unchecked")
	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		// License l = new License();
		// if (l.check()) {
		setupMoney();
		plotworldname = get().getConfig().getString("PlotWorld_name");
		if (this.getServer().getPluginManager().getPlugin("PlotSquared") == null) {
			Bukkit.getConsoleSender().sendMessage(
					"§fO plugin est\u00e1 sendo desabilitado...");
			Bukkit.getConsoleSender().sendMessage(
					"§fO plugin 'PlotSquared' n\u00e3o foi encontrado.");
			this.getServer().getPluginManager().disablePlugin((Plugin) this);
			return;
		}
		this.getCommand("terreno").setExecutor((CommandExecutor) new Terreno());
		this.getServer().getPluginManager().registerEvents((Listener) new Events(), (Plugin) this);
		ped = (ArrayList<Pedreiro>) FileManager.load(new File(getDataFolder(), "delay.dat"));
		if (ped == null) {
			ped = new ArrayList<>();
		} else {
			for (Pedreiro p : ped)
				p.start();
		}
		Corretor.terrains = (ArrayList<Corretor>) FileManager.load(new File(getDataFolder(), "corretor.dat"));
		if (Corretor.terrains == null)
			Corretor.terrains = new ArrayList<>();
		// }
	}

	public void onDisable() {
		FileManager.save(ped, new File(getDataFolder(), "delay.dat"));
		FileManager.save(Corretor.terrains, new File(getDataFolder(), "corretor.dat"));
	}

	public static String format(final double value) {
		return Formater.format(new Double(value).longValue());
	}

	public static Plugin get() {
		return plugin;
	}

	public boolean setupMoney() {
		final RegisteredServiceProvider<Economy> permissionProvider = (RegisteredServiceProvider<Economy>) Bukkit
				.getServer().getServicesManager().getRegistration(Economy.class);
		if (permissionProvider != null) {
			Main.economy = (Economy) permissionProvider.getProvider();
		}
		return Main.economy != null;
	}

}
