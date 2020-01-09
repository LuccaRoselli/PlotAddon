package com.devlucca.plotgui.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.devlucca.plotgui.Main;
import com.devlucca.plotgui.usefull.HeadBuilder;
import com.devlucca.plotgui.usefull.ItemBuilder;
import com.devlucca.plotgui.usefull.Methods;
import com.devlucca.plotgui.usefull.SlotGetter;

public class PreBuilds {
	
	@SuppressWarnings("deprecation")
	private static ItemStack build(String item){
		if (Main.get().getConfig().getString("Construcoes." + item + ".item.id").contains("headbase-")){
			String base64 = Main.get().getConfig().getString("Construcoes." + item + ".item.id").replace("headbase-", "");
			ArrayList<String> lore = new ArrayList<>();
			for (String s : Main.get().getConfig().getStringList("Construcoes." + item + ".item.lore"))
				lore.add(s.replace("&", "§").replace("%price", Methods.formatarNumero(Main.get().getConfig().getDouble("Construcoes." + item + ".preço"))));
			return new HeadBuilder().head(base64).name("§e" + item).lore(lore).build();
		} else {
			if (Main.get().getConfig().getString("Construcoes." + item + ".item.id").contains(":")){
				String[] material = Main.get().getConfig().getString("Construcoes." + item + ".item.id").split(":");
				ArrayList<String> lore = new ArrayList<>();
				for (String s : Main.get().getConfig().getStringList("Construcoes." + item + ".item.lore"))
					lore.add(s.replace("&", "§").replace("%price", Methods.formatarNumero(Main.get().getConfig().getDouble("Construcoes." + item + ".preço"))));
				return new ItemBuilder(Material.getMaterial(Integer.parseInt(material[0]))).durability(Integer.parseInt(material[1])).lore(lore).build();
			} else {
				String[] material = Main.get().getConfig().getString("Construcoes." + item + ".item.id").split(":");
				ArrayList<String> lore = new ArrayList<>();
				for (String s : Main.get().getConfig().getStringList("Construcoes." + item + ".item.lore"))
					lore.add(s.replace("&", "§").replace("%price", Methods.formatarNumero(Main.get().getConfig().getDouble("Construcoes." + item + ".preço"))));
				return new ItemBuilder(Material.getMaterial(Integer.parseInt(material[0]))).lore(lore).build();
			}
		}
	}
	
	public static void openInv2(Player p){
		ArrayList<ItemStack> itens = new ArrayList<>();
    	if (Main.get().getConfig().isSet("Construcoes")){
    		for (String key : Main.get().getConfig().getConfigurationSection("Construcoes").getKeys(false)){
    			itens.add(build(key));
    		}
    	}
    	int i = itens.size() / 7;
		Inventory inv = Bukkit.createInventory(null, (int)(i < 0 ? 27 : (9 * (3 + i) + 9 > 54 ? 54 : 9 * (3 + i) + 9)), "Escolha a construção");
		for (ItemStack is : itens){
			inv.setItem(SlotGetter.getSlot(inv), is);
		}
    	inv.setItem((itens.size() < 0) ? 13 : (9 * (3 + itens.size() / 7) + 4), new ItemBuilder(Material.ARROW).name("§cVoltar").build());
    	p.openInventory(inv);
	}
	
	public static void openInv(Player p){
		ArrayList<ItemStack> itens = new ArrayList<>();
    	if (Main.get().getConfig().isSet("Construcoes")){
    		for (String key : Main.get().getConfig().getConfigurationSection("Construcoes").getKeys(false)){
    			itens.add(build(key));
    		}
    	}
    	int i = itens.size() / 7;
		Inventory inv = Bukkit.createInventory(null, (int)(i < 0 ? 27 : (9 * (3 + i) + 9 > 54 ? 54 : 9 * (3 + i) + 9)), "§0Escolha a construção");
		for (ItemStack is : itens){
			inv.setItem(SlotGetter.getSlot(inv), is);
		}
    	inv.setItem((itens.size() < 0) ? 13 : (9 * (3 + itens.size() / 7) + 4), new ItemBuilder(Material.ARROW).name("§cVoltar").build());
    	p.openInventory(inv);
	}

}
