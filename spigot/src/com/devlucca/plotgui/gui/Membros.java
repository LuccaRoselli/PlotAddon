package com.devlucca.plotgui.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.devlucca.plotgui.usefull.ItemBuilder;

public class Membros {

	public static void open(Player player) {
		Inventory inv = org.bukkit.Bukkit.createInventory(null, 36, "Terrenos: Membros");
		inv.setItem(11, new ItemBuilder().head("http://textures.minecraft.net/texture/8d5427a83540a08a3fa2e655c2964a07243514584a71ec35d6b9e184dfbe318").name("§eConfiados").lore("§7Clique aqui para ver", "§7os membros confiados ao terreno.").build());
		inv.setItem(13, new ItemBuilder().head("http://textures.minecraft.net/texture/f58c975661c3e94fc35115648158e3ee9f80df74a4399e4d31ca5dbbcaf29b6").name("§eMembros").lore("§7Clique aqui para ver", "§7os membros do terreno.").build());
		inv.setItem(15, new ItemBuilder().head("http://textures.minecraft.net/texture/49f1f07e2b1c32bb64a128e529f3af1e5286e518544edf8cbaa6c4065b476b").name("§eNegados").lore("§7Clique aqui para ver", "§7os membros negados ao terreno.").build());
		inv.setItem(31, new ItemBuilder(Material.ARROW).name("§7Voltar").build());
		player.openInventory(inv);
	}
	
}
