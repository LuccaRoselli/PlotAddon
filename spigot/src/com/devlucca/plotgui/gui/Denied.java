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
import com.intellectualcrafters.plot.util.UUIDHandler;

public class Denied {
	
    public static void open(final Player p) {
        final List<ItemStack> itens = new ArrayList<ItemStack>();
        Methods.getPlot(p.getLocation()).getDenied().forEach(c -> itens.add(new ItemBuilder().owner(UUIDHandler.getName(c)).name("§7" + UUIDHandler.getName(c)).build()));
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, (itens.size() < 0) ? 27 : (9 * (3 + itens.size() / 7) + 9), "Usuários negados");
        itens.forEach(c -> inv.setItem(Methods.getNextSlot(inv), c));
        inv.setItem((itens.size() < 0) ? 13 : (9 * (3 + itens.size() / 7) + 4), new ItemBuilder(Material.ARROW).name("§7Voltar").build());
        p.openInventory(inv);
    }

}
