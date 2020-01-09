package com.devlucca.plotgui.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.devlucca.plotgui.usefull.ItemBuilder;

public class Confirmações {
	
	public static void openDispose(Player p){
		Inventory inv = org.bukkit.Bukkit.createInventory(null, 27, "Você tem certeza? (Excluir)");
		inv.setItem(12, new ItemBuilder(Material.WOOL).durability(14).name("§cCancelar").build());
		inv.setItem(13, new ItemBuilder(Material.BARRIER).name("§eExcluir terreno").lore("§7Clique para excluir permanentemente", "§7seu terreno!", "", "§7§oCuidado! isso irá deixar o seu", "§7§oterreno livre para outros comprarem!").build());
		inv.setItem(14, new ItemBuilder(Material.WOOL).durability(5).name("§aConfirmar").build());
		p.openInventory(inv);
	}
	
	public static void openClear(Player p){
		Inventory inv = org.bukkit.Bukkit.createInventory(null, 27, "Você tem certeza? (Limpar)");
		inv.setItem(12, new ItemBuilder(Material.WOOL).durability(14).name("§cCancelar").build());
		inv.setItem(13, new ItemBuilder(Material.IRON_SPADE).name("§eLimpar terreno").lore("§7Clique para resetar o plot ao natural", "", "§7§oCuidado! isso irá apagar todas", "§7§oas construções realizadas.").build());
		inv.setItem(14, new ItemBuilder(Material.WOOL).durability(5).name("§aConfirmar").build());
		p.openInventory(inv);
	}

}
