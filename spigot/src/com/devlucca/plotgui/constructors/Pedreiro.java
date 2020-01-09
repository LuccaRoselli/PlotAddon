package com.devlucca.plotgui.constructors;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.devlucca.plotgui.Main;
import com.devlucca.plotgui.usefull.ActionBar;
import com.devlucca.plotgui.usefull.Methods;
import com.devlucca.plotgui.usefull.SerializableLocation;
import com.intellectualcrafters.plot.object.Plot;

@SuppressWarnings("serial")
public class Pedreiro implements Serializable {

	String player;
	List<SerializableLocation> blocks;
	double total = 0;
	int fase = -10;

	public Pedreiro(Player p) {
		this.player = p.getName();
		Location l1 = null;
		Location l2 = null;
		Plot plot = Methods.getPlot(p.getLocation());
		if (!plot.isMerged()) {
			l1 = new Location(Bukkit.getWorld(plot.getTopAbs().getWorld()), plot.getTopAbs().getX(),
					plot.getTopAbs().getY(), plot.getTopAbs().getZ());
			l2 = new Location(Bukkit.getWorld(plot.getBottomAbs().getWorld()), plot.getBottomAbs().getX(),
					plot.getBottomAbs().getY(), plot.getBottomAbs().getZ());
		} else {
			ArrayList<Plot> plots = new ArrayList<>();
			for (Plot pl : plot.getConnectedPlots()){
				plots.add(pl);
			}
			Plot p1 = plots.get(0);
			Plot p2 = plots.get(plots.size() - 1);
			l1 = new Location(Bukkit.getWorld(p1.getTopAbs().getWorld()), p1.getTopAbs().getX(),
					p1.getTopAbs().getY(), p1.getTopAbs().getZ());
			l2 = new Location(Bukkit.getWorld(p2.getBottomAbs().getWorld()), p2.getBottomAbs().getX(),
					p2.getBottomAbs().getY(), p2.getBottomAbs().getZ());
		}
		List<SerializableLocation> blist = blocksFromTwoPoints(l2, l1);
		this.blocks = blist;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Pedreiro.this.total = blocks.size();
			}
		}.runTaskLater(Main.get(), 15L);
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(this.player);
	}

	public String getPlayerName(){
		return this.player;
	}
	
	String atual = "Limpando.";

	public String getByPhase(int phase) {
		if (phase < 10) {
			atual = "Limpando.";
		} else if (phase > 10 && phase < 29) {
			atual = "Limpando..";
		} else if (phase > 30 && phase < 49) {
			atual = "Limpando...";
		} else if (phase > 50 && phase < 79) {
			atual = "Limpando...";
		} else if (phase > 80 && phase < 109) {
			atual = "Limpando..";
		} else if (phase > 110 && phase < 139) {
			atual = "Limpando.";
		} else if (phase > 140 && phase < 169) {
			atual = "Limpando";
		} else if (phase > 170 && phase < 199) {
			fase = -20;
		}
		return atual;
	}

	public List<SerializableLocation> getBlocks(){
		return this.blocks;
	}
	
	public synchronized void start() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.get(), new Runnable() {
			@Override
			public void run() {
				if (!blocks.isEmpty()) {
					for (int i = 0; i < Math.floor(Math.random() * 8) + 1; i++) {
						if (blocks.size() - 1 >= 0) {
							Block b = blocks.get(blocks.size() - 1).getBlock();
							new SyncBlockChanger(b, Material.AIR).run();
							blocks.remove(SerializableLocation.getMTLocationFromLocation(b.getLocation()));
						}
					}
					start();
				}
			}
		}, 1);
		double atual = blocks.size();
		double percent = ((atual * 100) / total);
		fase++;
		if (getPlayer() != null && getPlayer().isOnline()) {
			if (blocks.size() > 0) {
				ActionBar.send(getPlayer(), "§e" + getByPhase(fase) + "§f - §eProgresso §f"
						+ new DecimalFormat("#.#").format(100 - percent) + "% §e(§f" + blocks.size() + " blocos§e)");
			} else {
				ActionBar.send(getPlayer(), "§aLimpeza finalizada!");
				getPlayer().playSound(getPlayer().getLocation(), Sound.LEVEL_UP, 1F, 1F);
				getBlocks().clear();
				Main.ped.remove(this);
			}
		}
	}

	/*
	private List<SerializableLocation> blocksFromTwoPoints(Location loc1, Location loc2) {
		List<SerializableLocation> blocks = new ArrayList<SerializableLocation>();
		Cuboid c = new Cuboid(loc1, loc2);
		for (Block b : c.getBlocks())
			if (b.getType() != Material.AIR)
				if (b.getType() != Material.BEDROCK)
					blocks.add(SerializableLocation.getMTLocationFromLocation(b.getLocation()));
		return blocks;
	}
	*/
	
	private List<SerializableLocation> blocksFromTwoPoints(Location loc1, Location loc2) {
		List<SerializableLocation> blocks = new ArrayList<SerializableLocation>();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
				int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

				int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
				int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

				int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
				int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

				for (int y = bottomBlockY; y <= topBlockY; y++) {
					for (int z = bottomBlockZ; z <= topBlockZ; z++) {
						for (int x = bottomBlockX; x <= topBlockX; x++) {
							Block block = loc1.getWorld().getBlockAt(x, y, z);
							if (block.getType() != Material.AIR)
								if (block.getType() != Material.BEDROCK)
									blocks.add(SerializableLocation.getMTLocationFromLocation(block.getLocation()));
						}
					}
				}
			}
		}.runTaskAsynchronously(Main.get());
		return blocks;
	}

	public <T> T getFirstElement(final Iterable<T> elements) {
		if (elements == null)
			return null;

		return elements.iterator().next();
	}

	public <T> T getLastElement(final Iterable<T> elements) {
		final Iterator<T> itr = elements.iterator();
		T lastElement = itr.next();

		while (itr.hasNext()) {
			lastElement = itr.next();
		}

		return lastElement;
	}
	
	public class SerializableLoc{
		int x,y,z;
		String world;
		
		public SerializableLoc(int x, int y, int z, String world){
			this.x = x;
			this.y = y;
			this.z = z;
			this.world = world;
		}
	}

	public class SyncBlockChanger implements Runnable{
		  
	    private Block block;
	    private Material material;

	    public SyncBlockChanger(Block block, Material material){
	        this.block = block;
	        this.material = material;
	    }
	  
	    @Override
	    public void run() {
	        block.setType(material);
	    }
	}
	
}
