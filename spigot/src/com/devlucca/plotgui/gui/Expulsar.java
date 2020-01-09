package com.devlucca.plotgui.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.devlucca.plotgui.usefull.ItemBuilder;
import com.devlucca.plotgui.usefull.Methods;
import com.intellectualcrafters.plot.object.PlotPlayer;

public class Expulsar {
	
    public static void open(final Player p) {
        final List<ItemStack> itens = new ArrayList<ItemStack>();
        for (PlotPlayer c : Methods.getPlot(p.getLocation()).getPlayersInPlot()){
        	if (c.getName().equals(p.getName())) continue;
        	itens.add(new ItemBuilder().owner(c.getName()).name("§e" + c.getName()).lore("§7Clique para expulsar este", "§7jogador de seu terreno").build());
        }
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, (itens.size() < 0) ? 27 : (9 * (3 + itens.size() / 7) + 9), "Expulsar membros");
        itens.forEach(c -> inv.setItem(Methods.getNextSlot(inv), c));
        inv.setItem((itens.size() < 0) ? 13 : (9 * (3 + itens.size() / 7) + 4), new ItemBuilder(Material.ARROW).name("§7Voltar").build());
        p.openInventory(inv);
    }

}
