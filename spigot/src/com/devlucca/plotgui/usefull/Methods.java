package com.devlucca.plotgui.usefull;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.devlucca.plotgui.Main;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import com.plotsquared.bukkit.util.BukkitUtil;

public class Methods {

	public static PlotAPI getPlotAPI() {
		PlotAPI plot = new PlotAPI();
		return plot;
	}

	public static Plot getPlot(Location location) {
		return getPlotAPI().getPlot(location);
	}

	public static PlotPlayer getPlotPlayer(Player p) {
		return BukkitUtil.getPlayer(p);
	}

	private static final List<Integer> ints = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28,
			29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

	public static int getNextSlot(Inventory inventory) {
		int i = 0;
		while (i != 53) {
			if (ints.contains(i) && (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR)) {
				return i;
			}
			++i;
		}
		return 0;
	}

	public static int getLimit(Player p) {
		int limite = 0;
		if (p.hasPermission("*") || p.isOp()){
			limite = 9999;
		}else{
			for (PermissionAttachmentInfo perm : p.getEffectivePermissions()){
				if (perm.getPermission().toLowerCase().startsWith("plots.plot.")){
					if (Integer.parseInt(perm.getPermission().toLowerCase().replace("plots.plot.", "").trim()) > limite){
						limite = Integer.parseInt(perm.getPermission().toLowerCase().replace("plots.plot.", "").trim());
					}
				}
			}
		}
		return limite;
	}
	
	public static void depositPlayer(Player p, long value){
		double remaing = value / Double.MAX_VALUE;
		while(remaing > 1.0){			
			Main.economy.depositPlayer(p, 1.0 * Double.MAX_VALUE);			
			remaing -= 1.0d;
		}
		Main.economy.depositPlayer(p, remaing * Double.MAX_VALUE);
	}
	
	public static void withdrawPlayer(Player p, long value){
		double remaing = value / Double.MAX_VALUE;
		while(remaing > 1.0){			
			Main.economy.withdrawPlayer(p, 1.0 * Double.MAX_VALUE);			
			remaing -= 1.0d;
		}
		Main.economy.withdrawPlayer(p, remaing * Double.MAX_VALUE);
	}

	public static String getPrefix(){
		return Main.get().getConfig().getString("Prefixo").replace("&", "§");
	}
	
	public static String formatarNumero(final int numero) {
		return Main.format(numero);
	}

	public static String formatarNumero(final double numero) {
		return Main.format(numero);
	}

	public static String removerCores(final String frase) {
		final String coresremovidas = frase.replace("&0", "").replace("&1", "").replace("&2", "").replace("&3", "")
				.replace("&4", "").replace("&5", "").replace("&6", "").replace("&7", "").replace("&8", "")
				.replace("&9", "").replace("&a", "").replace("&b", "").replace("&c", "").replace("&d", "")
				.replace("&e", "").replace("&f", "").replace("&r", "");
		return coresremovidas;
	}

	public static boolean \u00e9Numero(final String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e2) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public static void add(final String p, final int valor) {
//		EconomyAPI.giveMoney(p, valor);
		Main.economy.depositPlayer(Bukkit.getOfflinePlayer(p), valor);
	}

	@SuppressWarnings("deprecation")
	public static void remove(final String p, final int valor) {
//		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), 
//				Main.get().getConfig().getString("Comandos.Remove_money") + " " + p + " " + String.valueOf(valor));
//		EconomyAPI.removeMoney(p, valor);
		Main.economy.withdrawPlayer(Bukkit.getOfflinePlayer(p), valor);
	}

	@SuppressWarnings("deprecation")
	public static double get(final String p) {
		return Double.valueOf(Main.economy.getBalance(p));
	}

}
