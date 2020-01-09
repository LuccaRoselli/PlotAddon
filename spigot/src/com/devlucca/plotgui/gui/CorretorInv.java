package com.devlucca.plotgui.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.devlucca.plotgui.Main;
import com.devlucca.plotgui.constructors.Corretor;
import com.devlucca.plotgui.reflection.NbtApi;
import com.devlucca.plotgui.usefull.ItemBuilder;
import com.devlucca.plotgui.usefull.Methods;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.util.UUIDHandler;

public class CorretorInv {
	
    public static Map<String, Integer> page_opened = new HashMap<String, Integer>();
    public static Map<String, Plot> imovel = new HashMap<String, Plot>();
    
    
    @SuppressWarnings("deprecation")
	public static void getCorretor(Player p, int page) {
        page_opened.put(p.getName(), page);
        ArrayList<Corretor> list = new ArrayList<Corretor>();
        int i = page == 1 ? 0 : (page - 1) * 28;
        while (i < Corretor.terrains.size()) {
            list.add(Corretor.terrains.get(i));
            ++i;
        }
        int size = list.size() / 7	;
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, (int)(9 * (3 + size) > 54 ? 54 : 9 * (3 + size)), (String)("Terrenos a venda: (Página " + page + ")"));
        list.stream().forEach(c -> {
            String name = UUIDHandler.getName(Methods.getPlot(new Location(Bukkit.getWorld(c.getWorld()), c.getX(), c.getY(), c.getZ())).owner);
            if (inv.getSize() == 54 ? inv.getItem(43) == null : inv.getItem(0) == null) {
            	ItemStack itemcorretor;
            	if (p.getName().equals(name))
            		itemcorretor = new ItemBuilder().head("http://textures.minecraft.net/texture/f620519b74536c1f85b7c7e5e11ce5c059c2ff759cb8df254fc7f9ce781d29").name("§eTerreno §ede §f" + name).lore("§f§lPREÇO: §7R$" + Main.format(c.getPrice()), "", "§f§l* §7Este terreno é seu!", "","§7Clique §fSHIFT §7+ §fESQUERDO", "§7para cancelar a venda").build();
            	else
            		itemcorretor = new ItemBuilder().head("http://textures.minecraft.net/texture/f620519b74536c1f85b7c7e5e11ce5c059c2ff759cb8df254fc7f9ce781d29").name("§eTerreno §ede §f" + name).lore("§f§lPREÇO: §7R$" + Main.format(c.getPrice()), "", "§7Clique para ver mais informações").build();
            	itemcorretor = NbtApi.setNBTData(itemcorretor,"x","§fX: " + c.getX());
            	itemcorretor = NbtApi.setNBTData(itemcorretor,"y","§fY: " + c.getY());
            	itemcorretor = NbtApi.setNBTData(itemcorretor,"z","§fZ: " + c.getZ());
            	itemcorretor = NbtApi.setNBTData(itemcorretor,"preço", String.valueOf(c.getPrice()));
            	inv.setItem(Methods.getNextSlot(inv), itemcorretor);
            }
        }
        );
        if (inv.getSize() == 54 && inv.getItem(43) != null) {
            inv.setItem(35, new ItemBuilder().head(ItemBuilder.HeadsENUM.ARROW_RIGHT).name("§aPróxima página").build());
        }
        if (page > 1) {
            inv.setItem(inv.getSize() == 54 ? 27 : 9 * (3 + size) - 18, new ItemBuilder().head(ItemBuilder.HeadsENUM.ARROW_LEFT).name("§aPágina anterior").build());
        }
        p.openInventory(inv);
    }

	@SuppressWarnings("deprecation")
	public static void getOptionsCorretor(Player p, ItemStack is) {	
        Location loc = new Location(Bukkit.getWorld(Main.plotworldname), Integer.valueOf(NbtApi.getTag(is,"x").replace("§fX: ", "")), Integer.valueOf(NbtApi.getTag(is,"y").replace("§fY: ", "")), Integer.valueOf(NbtApi.getTag(is,"z").replace("§fZ: ", "")));
        imovel.put(p.getName(), Methods.getPlot(loc));
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, (int)36, (String)"Terreno a venda: " + is.getItemMeta().getDisplayName().split("§f")[1]);
        ItemStack comprar = new ItemBuilder().head("http://textures.minecraft.net/texture/d04719b3b97d195a205718b6ee21f5c95cafa167e7abca88a2103d52b37d722").name("§eComprar o terreno").build();
        comprar = NbtApi.setNBTData(comprar, "preço", NbtApi.getTag(is, "preço"));
        inv.setItem(11, new ItemBuilder().head("http://textures.minecraft.net/texture/49f48060a96cdeae3cb4173d24f5431704b2b12448d95f31d71cbe4a7b8deb").name("§eIr at\u00e9 o terreno").build());
        inv.setItem(13, new ItemBuilder(Material.getMaterial(175)).name("§ePreço").lore("§7R$" + Methods.formatarNumero(Integer.valueOf(NbtApi.getTag(is, "preço")))).build());
        inv.setItem(15, comprar);
        inv.setItem(31, new ItemBuilder(Material.ARROW).name("§7Voltar").build());
        p.openInventory(inv);
    }

}
