package com.devlucca.plotgui.constructors;

import java.io.Serializable;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.devlucca.plotgui.usefull.Methods;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.util.UUIDHandler;

@SuppressWarnings("serial")
public class Corretor implements Serializable{
	private int price;
	public static List<Corretor> terrains;
	private int x, y, z;
	private String world;

	public Corretor(final Player p, final int price) {
		this.price = price;
		this.x = (int) p.getLocation().getX();
		this.y = (int) p.getLocation().getY();
		this.z = (int) p.getLocation().getZ();
		this.world = p.getLocation().getWorld().getName();
		Corretor.terrains.add(this);
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getZ() {
		return this.z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public String getWorld() {
		return this.world;
	}
	
	public int getPrice() {
		return this.price;
	}

	public void setPrice(final int price) {
		this.price = price;
	}
	
	public void destroy(Player p, boolean mensagem){
		Corretor.terrains.remove(this);
		if (mensagem)
			p.sendMessage(Methods.getPrefix() + "§7Você cancelou a venda do seu terreno.");
		p.closeInventory();
	}

	@SuppressWarnings("deprecation")
	public static void comprarTerreno(Player p, Plot terrain, int preço) {
		if (Methods.get(p.getName()) < (double) preço) {
			p.sendMessage(Methods.getPrefix() + "§cVocê precisa de §f$" + Methods.formatarNumero((int) preço)
					+ " §cpara poder comprar este terreno.");
		} else {
        	if (Methods.getPlotPlayer(p).getPlotCount() + 1 > Methods.getLimit(p)){
        		p.sendMessage(Methods.getPrefix() + "§cLimite de terreno excedido! (Seu limite é: " + Methods.getLimit(p) + ")");
        		p.sendMessage(Methods.getPrefix() + "§cAdquira já seu VIP em nossa loja para expandir seu limite de terrenos.");
        		return;
        	}
        	
			Methods.remove(p.getName(), preço);
			p.sendMessage(Methods.getPrefix() + "§7Você gastou §f$" + Methods.formatarNumero((int) preço)
			+ " §7comprando um terreno.");
			Corretor.terrains.stream().
				filter(c -> Methods.getPlot(new Location(Bukkit.getWorld(c.getWorld()), c.getX(), c.getY(), c.getZ()))
						.equals(terrain))
							.findFirst().get().destroy(p, false);
			String name = UUIDHandler.getName(terrain.owner);
			Methods.add(name, preço);
			if (Bukkit.getPlayer(name) != null && Bukkit.getPlayer(name).isOnline()){
				Bukkit.getPlayer(name).sendMessage(Methods.getPrefix() + "§7O usuário §f" + p.getName() + " §7comprou um terreno seu!");
				Bukkit.getPlayer(name).sendMessage(Methods.getPrefix() + "§7Você ganhou §a$" + Methods.formatarNumero((int) preço) + "§7.");
			}
			terrain.getMembers().clear();
			terrain.getDenied().clear();
			terrain.getTrusted().clear();
			terrain.setOwner(p.getUniqueId());
			
		}
	}

	@SuppressWarnings("deprecation")
	public static void teleportRegionCorretor(Plot region, Player p) {
		Location l = new Location(Bukkit.getWorld(region.getHome().getWorld()), region.getHome().getX(),
				region.getHome().getY(), region.getHome().getZ());
		l.setPitch(region.getHome().getPitch());
		l.setYaw(region.getHome().getYaw());
		p.teleport(l);
		p.sendMessage(Methods.getPrefix() + "§7Você foi teletransportado até o terreno de §e" + UUIDHandler.getName(region.owner)
				+ "§7.");
	}

	public static void venderTerreno(final Player p, final int preco) {
		p.sendMessage(
				Methods.getPrefix() + "§7Você colocou seu terreno a venda por §a$" + Methods.formatarNumero(preco) + "§7.");
		new Corretor(p, preco);
	}

}