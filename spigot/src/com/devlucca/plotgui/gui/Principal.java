package com.devlucca.plotgui.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.devlucca.plotgui.Main;
import com.devlucca.plotgui.usefull.Formater;
import com.devlucca.plotgui.usefull.ItemBuilder;
import com.devlucca.plotgui.usefull.Methods;
import com.intellectualcrafters.plot.object.Plot;

public class Principal {
	

	@SuppressWarnings("deprecation")
	public static void open(Player player) {
		Inventory inv = org.bukkit.Bukkit.createInventory(null, 54, "Menu dos terrenos");
		inv.setItem(10, new ItemBuilder().head("http://textures.minecraft.net/texture/f8ad7d964d691363ef2af8af2eaa5b4315237510e81fabd9acc9d4e27294").name("§eMembros").lore("§7Clique aqui para ver os membros", "§7que fazem parte do terreno.").build());
		inv.setItem(11, new ItemBuilder().head("http://textures.minecraft.net/texture/25e9152efd892f60d7e0d7e53369e04779ed3111e2fb2752b6f4c26df540aedc").name("§eExpulsar membro").lore("§7Clique para expulsar um membro", "§7que está em seu terreno.").build());
		inv.setItem(14, new ItemBuilder().head("http://textures.minecraft.net/texture/361e5b333c2a3868bb6a58b6674a2639323815738e77e053977419af3f77").name("§eConfiar").lore("§7Clique para confiar um membro", "§7em seu terreno.", "", "§7§oCom essa permissão ele poderá construir", "§7§oem seu plot enquanto você estiver offline.").build());
		inv.setItem(15, new ItemBuilder().head("http://textures.minecraft.net/texture/36f69f7b7538b41dc3439f3658abbd59facca366f190bcf1d6d0a026c8f96").name("§eAdicionar").lore("§7Clique para adicionar um membro", "§7ao seu terreno.", "", "§7§oCom essa permissão ele poderá construir", "§7§oem seu plot apenas quando você estiver online.").build());
		inv.setItem(16, new ItemBuilder().head("http://textures.minecraft.net/texture/1919d1594bf809db7b44b3782bf90a69f449a87ce5d18cb40eb653fdec2722").name("§eNegar").lore("§7Clique para negar um membro", "§7em seu terreno.", "", "§7§oCom essa permissão ele não poderá", "§7§onem entrar em seu terreno.").build());
		inv.setItem(19, new ItemBuilder(Material.IRON_SPADE).name("§eLimpar terreno").lore("§7Clique para resetar o plot ao natural.", "", "§7§oCuidado! isso irá apagar todas", "§7§oas construções realizadas.", "",  "§f§lCUSTO: " + Main.get().getConfig().getString("Economia.Lore_Custo").replace("&", "§").replace("%valor%",Formater.format(Main.get().getConfig().getLong("Economia.Preço_Limpar")))).build());
		inv.setItem(20, new ItemBuilder(Material.DIAMOND_SPADE).name("§ePedreiro").lore("§7Clique para limpar seu plot inteiro", "§7do chão até a bedrock.", "", "§f§lCUSTO: §7" + Main.get().getConfig().getString("Economia.Lore_Custo").replace("&", "§").replace("%valor%", Formater.format(Main.get().getConfig().getLong("Economia.Preço_Limpar_Pedreiro")))).build());
		inv.setItem(24, new ItemBuilder().head("http://textures.minecraft.net/texture/4bac77520b9eee65068ef1cd8abeadb013b4de3953fd29ac68e90e4866227").name("§eRemover").lore("§7Clique para remover todas as permissões", "§7de um membro em seu terreno.").build());
		//inv.setItem(25, new ItemBuilder(Material.ANVIL).name("§eVisitar amigo").lore("§7Clique para visitar o terreno", "§7de um amigo.").build());
		inv.setItem(28, new ItemBuilder(Material.BARRIER).name("§eExcluir terreno").lore("§7Clique para excluir permanentemente", "§7seu terreno!", "", "§7§oCuidado! isso irá deixar o seu", "§7§oterreno livre para outros comprarem!").build());
		inv.setItem(29, new ItemBuilder(Material.getMaterial(175)).name("§eVender terreno").lore("§7Clique para anunciar seu", "§7terreno à venda!", "", "§f§l* §7Usuários poderão compra-lo", "§7digitando §f/terreno corretor§7!").build());
		inv.setItem(49, new ItemBuilder(Material.ARROW).name("§7Voltar").build());
		player.openInventory(inv);
	}
	
	public static void getPanel(Player p) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)null, (int)27, (String)("Painel: " + p.getName()));
            ArrayList<ItemStack> itens = new ArrayList<ItemStack>();
            int id = 0;
            int mergednumber = 0;
            for (Plot t : Methods.getPlotAPI().getPlayerPlots(p)) {
            	id++;
            	if (t.isMerged()){
            		mergednumber++;
            		id--;
            		itens.add(new ItemBuilder(Material.AIR).build());
            	} else {
            		itens.add(new ItemBuilder().head("http://textures.minecraft.net/texture/c95d37993e594082678472bf9d86823413c250d4332a2c7d8c52de4976b362").name("\u00a7eTerreno - #0" + id).lore("§7Clique para se teleportar", "§7ao terreno escolhido.").build());
            	}
            	int i = itens.size() / 7;
                inventory = Bukkit.createInventory((InventoryHolder)null, (int)(i < 0 ? 27 : (9 * (3 + i) + 9 > 54 ? 54 : 9 * (3 + i) + 9)), (String)("Painel: " + p.getName()));
                for (ItemStack is : itens) {
                    if (!(inventory.getSize() == 54 ? inventory.getItem(34) == null : inventory.getItem(0) == null)) continue;
                    inventory.setItem(Methods.getNextSlot(inventory), is);
                }
                if (Methods.getPlotAPI().getPlayerPlots(p).isEmpty()) continue;
                inventory.setItem(9 * (3 + i) + 3, new ItemBuilder().head("http://textures.minecraft.net/texture/9271809ba91b42fb4875af4ba298e5e55f45ed73721bcea85a45db92628574f").name("\u00a7eComprar terreno").lore("§7Clique para comprar um terreno.").build());
                inventory.setItem(9 * (3 + i) + 4, new ItemBuilder(Material.ANVIL).name("§eVisitar amigo").lore("§7Clique para visitar o terreno", "§7de um amigo.").build());
                inventory.setItem(9 * (3 + i) + 5, new ItemBuilder().head("http://textures.minecraft.net/texture/5123b88846d66e1cfe2f664a36ad4a22b1a4c2f2e4d295f41fe5e929b9e7d8").name("\u00a7eGerenciar terreno").lore("§7Clique para gerenciar o terreno", "§7em que você se encontra.").build());
            }
            
            if (mergednumber > 0){
            	mergednumber = mergednumber / 2;
            	while (mergednumber > 0){
            		mergednumber--;
            		inventory.setItem(Methods.getNextSlot(inventory), new ItemBuilder().head("http://textures.minecraft.net/texture/c95d37993e594082678472bf9d86823413c250d4332a2c7d8c52de4976b362").name("\u00a7eTerreno - #0" + (id + 1)).lore("§7Clique para se teleportar", "§7ao terreno escolhido.").build());
            	}
            }
            if (Methods.getPlotAPI().getPlayerPlots(p).isEmpty()){
            	inventory.setItem(12, new ItemBuilder().head("http://textures.minecraft.net/texture/9271809ba91b42fb4875af4ba298e5e55f45ed73721bcea85a45db92628574f").name("\u00a7eComprar terreno").lore("§7Clique para comprar um terreno").build());
            	inventory.setItem(14, new ItemBuilder(Material.SIGN).name("§eVisitar amigo").lore("§7Clique para visitar o terreno", "§7de um amigo.").build());
            }
        p.openInventory(inventory);
    }
	
}
