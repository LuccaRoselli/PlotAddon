package com.devlucca.plotgui.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.devlucca.plotgui.usefull.ItemBuilder;
import com.devlucca.plotgui.usefull.ItemBuilder.HeadsENUM;

public class ObterTerreno {
	
	public static void open(Player player) {
		Inventory inv = org.bukkit.Bukkit.createInventory(null, 36, "Obter terreno");
		inv.setItem(11, new ItemBuilder().head(HeadsENUM.QUESTION_NEW).name("§eRandômicamente").lore("§7Clique aqui para adquirir", "§7um terreno randômicamente.", "", "§f§lAVISO:", "§7Clique §eESQUERDO §7para comprar um terreno simples", "§7Clique §eDIREITO §7para comprar um terreno costumizado").build());
		inv.setItem(15, new ItemBuilder().head("http://textures.minecraft.net/texture/c5a35b5ca15268685c4660535e5883d21a5ec57c55d397234269acb5dc2954f").name("§eEste terreno").lore("§7Clique aqui para adquirir", "§7este terreno que você se encontra.", "", "§f§lAVISO:", "§7Clique §eESQUERDO §7para comprar um terreno simples", "§7Clique §eDIREITO §7para comprar um terreno costumizado").build());
		inv.setItem(31, new ItemBuilder(Material.ARROW).name("§7Voltar").build());
		player.openInventory(inv);
	}
}
