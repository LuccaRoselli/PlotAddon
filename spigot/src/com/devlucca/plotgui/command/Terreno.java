package com.devlucca.plotgui.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.devlucca.plotgui.constructors.Corretor;
import com.devlucca.plotgui.gui.CorretorInv;
import com.devlucca.plotgui.gui.Principal;
import com.devlucca.plotgui.usefull.Methods;

public class Terreno implements CommandExecutor {
	public static List<String> mundos;
	public static List<String> confirma\u00e7\u00e3o;
	private static final String ONLY_PLAYERS = new String("§cApenas jogadores podem executar esse comando");;

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String l, final String[] args) {
		if (sender instanceof ConsoleCommandSender) {
		}
		if (sender instanceof Player) {
			final Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("terreno")) {
				if (args.length == 0) {
					Principal.getPanel(p);
				} else {
					if (args[0].equalsIgnoreCase("corretor")) {
						CorretorInv.getCorretor(p, 1);
						return false;
					}
					if (args[0].equalsIgnoreCase("vender")) {
						if (args.length == 1) {
							p.sendMessage(Methods.getPrefix() + "§7Use /terreno vender <preço>");
						} else {
							try {
								if (!(Methods.getPlotAPI().getPlot(p.getLocation()) == null))
									if (Methods.getPlotAPI().getPlot(p.getLocation()).isOwner(p.getUniqueId()))
										if (Corretor.terrains.stream()
												.filter(c -> Methods.getPlot(new Location(Bukkit.getWorld(c.getWorld()), c.getX(), c.getY(), c.getZ()))
														.equals(Methods.getPlot(p.getLocation())))
												.findFirst().orElse(null) == null)
											Corretor.venderTerreno(p, Integer.parseInt(args[1]));
										else
											p.sendMessage(Methods.getPrefix() + "§cEsse terreno já está a venda.");
									else
										p.sendMessage(Methods.getPrefix() + "§cEsse plot não é seu.");
								else
									p.sendMessage(Methods.getPrefix() + "§cVocê não está em um plot.");
							} catch (NumberFormatException e) {
								p.sendMessage(Methods.getPrefix() + "§7Por favor, indique o valor em números");
								return false;
							} catch (NullPointerException e2) {
								p.sendMessage(Methods.getPrefix() + "§7Por favor, indique o valor em números");
								return false;
							}

						}
						return false;
					}
				}
			}
		} else {
			sender.sendMessage(Terreno.ONLY_PLAYERS);
		}
		return false;
	}
}
