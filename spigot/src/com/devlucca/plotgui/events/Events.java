package com.devlucca.plotgui.events;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.devlucca.plotgui.Main;
import com.devlucca.plotgui.constructors.Corretor;
import com.devlucca.plotgui.constructors.Pedreiro;
import com.devlucca.plotgui.gui.Confirmações;
import com.devlucca.plotgui.gui.CorretorInv;
import com.devlucca.plotgui.gui.Denied;
import com.devlucca.plotgui.gui.Expulsar;
import com.devlucca.plotgui.gui.Members;
import com.devlucca.plotgui.gui.Membros;
import com.devlucca.plotgui.gui.ObterTerreno;
import com.devlucca.plotgui.gui.PreBuilds;
import com.devlucca.plotgui.gui.Principal;
import com.devlucca.plotgui.gui.Trusted;
import com.devlucca.plotgui.reflection.NbtApi;
import com.devlucca.plotgui.usefull.AnvilGUI;
import com.devlucca.plotgui.usefull.AnvilGUI.AnvilClickEvent;
import com.devlucca.plotgui.usefull.Methods;
import com.devlucca.plotgui.usefull.Schematic;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.util.UUIDHandler;
import com.plotsquared.bukkit.util.BukkitUtil;
import com.sk89q.worldedit.MaxChangedBlocksException;

public class Events implements Listener{
	
	private static HashMap<Player, String> terrenonumero = new HashMap<>();
	private static HashMap<Player, String> teleporters = new HashMap<>();
	private static HashMap<Player, Integer> xcorrection = new HashMap<>();
	private static HashMap<Player, Integer> ycorrection = new HashMap<>();
	private static HashMap<Player, Integer> zcorrection = new HashMap<>();
	
	@EventHandler
	public void ontp(PlayerTeleportEvent e){
		if (e.getTo().getWorld().getName().equalsIgnoreCase(Main.plotworldname)){
			if (teleporters.containsKey(e.getPlayer())){
				String schem = teleporters.get(e.getPlayer());
				teleporters.remove(e.getPlayer());
				Location l = e.getTo().clone();
				System.out.println(l);
				l.add(0, 0, 6);
				Plot plot = Methods.getPlot(l);
				com.intellectualcrafters.plot.object.Location center = plot.getCenter().clone();
				int x_fix = xcorrection.get(e.getPlayer());
				int y_fix = ycorrection.get(e.getPlayer());
				int z_fix = zcorrection.get(e.getPlayer());
				xcorrection.remove(e.getPlayer());
				ycorrection.remove(e.getPlayer());
				zcorrection.remove(e.getPlayer());
            	new BukkitRunnable() {
					
					@Override
					public void run() {
						try {
							Schematic.paste(center, false, schem, x_fix, y_fix, z_fix);
							e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1F, 1F);
						} catch (MaxChangedBlocksException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.runTaskLater(Main.get(), 20l);
			}
		}
	}
	
	
	@EventHandler
    public void onClick2(final InventoryClickEvent e) {
        if (e.getInventory().getTitle().startsWith("§0Escolha a") && e.getCurrentItem() != null) {
            final Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) {
                	ObterTerreno.open(p);
                } else {
                	String build = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                	double preço = Main.get().getConfig().getDouble("Construcoes." + build + ".preço");
                	if (Methods.get(p.getName()) < preço) {
            			p.sendMessage(Methods.getPrefix() + "§cVocê precisa de §f$" + Methods.formatarNumero(preço)
            					+ " §cpara poder utilizar essa opção.");
            			p.closeInventory();
            			return;
                	}
            		if (Methods.getPlotPlayer(p).getPlotCount() + 1 > Methods.getLimit(p)){
            			p.sendMessage(Methods.getPrefix() + "§cLimite de terreno excedido! (Seu limite é: " + Methods.getLimit(p) + ")");
            			p.sendMessage(Methods.getPrefix() + "§cAdquira já seu VIP em nossa loja para expandir seu limite de terrenos.");
            			return;
            		}
            		Methods.remove(p.getName(), (int)preço);
            		teleporters.put(p, Main.get().getConfig().getString("Construcoes." + build + ".schematic"));
            		ycorrection.put(p, Main.get().getConfig().getInt("Construcoes." + build + ".y_fix"));
            		xcorrection.put(p, Main.get().getConfig().getInt("Construcoes." + build + ".x_fix"));
            		zcorrection.put(p, Main.get().getConfig().getInt("Construcoes." + build + ".z_fix"));
            		p.performCommand("plot auto");
                }
            }
        }
	}
	
	@EventHandler
    public void onClick(final InventoryClickEvent e) {
        if (e.getInventory().getTitle().startsWith("Escolha a") && e.getCurrentItem() != null) {
            final Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cVoltar")) {
                	ObterTerreno.open(p);
                } else {
                	String build = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                	double preço = Main.get().getConfig().getDouble("Construcoes." + build + ".preço");
                	if (Methods.get(p.getName()) < preço) {
            			p.sendMessage(Methods.getPrefix() + "§cVocê precisa de §f$" + Methods.formatarNumero(preço)
            					+ " §cpara poder utilizar essa opção.");
            			p.closeInventory();
            			return;
                	}
            		if (Methods.getPlotPlayer(p).getPlotCount() + 1 > Methods.getLimit(p)){
            			p.sendMessage(Methods.getPrefix() + "§cLimite de terreno excedido! (Seu limite é: " + Methods.getLimit(p) + ")");
            			p.sendMessage(Methods.getPrefix() + "§cAdquira já seu VIP em nossa loja para expandir seu limite de terrenos.");
            			return;
            		}
            		Methods.remove(p.getName(), (int)preço);
            		p.performCommand("plot claim");
    				String schem = Main.get().getConfig().getString("Construcoes." + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()) + ".schematic");
    				teleporters.remove(p);
    				Location l = p.getLocation().clone();
    				System.out.println(l);
    				l.add(0, 0, 6);
    				Plot plot = Methods.getPlot(l);
    				com.intellectualcrafters.plot.object.Location center = plot.getCenter().clone();
    				int x_fix = Main.get().getConfig().getInt("Construcoes." + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()) + ".x_fix");
    				int y_fix = Main.get().getConfig().getInt("Construcoes." + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()) + ".y_fix");
    				int z_fix = Main.get().getConfig().getInt("Construcoes." + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()) + ".z_fix");
                	new BukkitRunnable() {
    					
    					@Override
    					public void run() {
    						try {
    							Schematic.paste(center, false, schem, x_fix, y_fix, z_fix);
    							p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1F);
    						} catch (MaxChangedBlocksException | IOException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    					}
    				}.runTaskLater(Main.get(), 20l);
                }
            }
        }
	}
	
	@EventHandler
    public void clickInventory(final InventoryClickEvent e) {
        if (e.getInventory().getTitle().startsWith("Painel: ") && e.getCurrentItem() != null) {
            final Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eComprar terreno")) {
                    ObterTerreno.open(p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eGerenciar terreno")) {
                	if (!(Methods.getPlotAPI().getPlot(p.getLocation()) == null))
                		if (Methods.getPlotAPI().getPlot(p.getLocation()).isOwner(p.getUniqueId()))
                			Principal.open(p);
                		else
                			p.sendMessage(Methods.getPrefix() + "§cEsse terreno não é seu.");
                	else
                		p.sendMessage(Methods.getPrefix() + "§cVocê não está em um terreno.");
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§eTerreno")) {
                    final String id = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§eTerreno - #0", "");
                    if (e.getClick() == ClickType.LEFT) {
                    	p.performCommand("plot home " + id);
                    }
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eVisitar amigo")) {
                	AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
                        
                        @Override
                        public void onAnvilClick(AnvilClickEvent event) {
                            if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                                event.setWillClose(true);
                                event.setWillDestroy(true);
                                if (terrenonumero.containsKey(p))
                                	terrenonumero.remove(p);
                                terrenonumero.put(p, event.getName());
                                new BukkitRunnable() {
											
									@Override
									public void run() {
					                	AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
					                        
					                        @Override
					                        public void onAnvilClick(AnvilClickEvent event) {
					                            if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
					                                event.setWillClose(true);
					                                event.setWillDestroy(true);
					                                p.closeInventory();
					                                new BukkitRunnable() {
																
														@Override
														public void run() {
															p.performCommand("plot home " + terrenonumero.get(p) + ":" + event.getName());
														}
													}.runTaskLater(Main.get(), 5L);
					                                
					                            }else{
					                                event.setWillClose(false);
					                                event.setWillDestroy(false);
					                            }
					                        }
					                    });
					                 
					                    ItemStack is = new ItemStack(Material.NAME_TAG);
					                    ItemMeta im = is.getItemMeta();
					                    im.setDisplayName("Número do terreno");
					                    is.setItemMeta(im);
					                 
					                    gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, is);
					                 
					                    try {
											gui.open();
										} catch (IllegalAccessException | InvocationTargetException | InstantiationException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
								}.runTaskLater(Main.get(), 5L);
                                
                            }else{
                                event.setWillClose(false);
                                event.setWillDestroy(false);
                            }
                        }
                    });
                 
                    ItemStack is = new ItemStack(Material.NAME_TAG);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName("Digite o usuário");
                    is.setItemMeta(im);
                 
                    gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, is);
                 
                    try {
						gui.open();
					} catch (IllegalAccessException | InvocationTargetException | InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }
        }
        if (e.getInventory().getTitle().startsWith("Você tem certeza? (Limpa") && e.getCurrentItem() != null) {
            final Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cCancelar")) {
                    Principal.open(p);             
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aConfirmar")) {
                	if (Methods.get(p.getName()) >= Main.get().getConfig().getDouble("Economia.Preço_Limpar")){
                    	Iterator<Pedreiro> iter = Main.ped.iterator();

                    	while (iter.hasNext()) {
                    	    Pedreiro str = iter.next();

                    	    if (str.getPlayerName().equalsIgnoreCase(p.getName())){
                    	        iter.remove();
                    	        str.getBlocks().clear();
                    	    }
                    	    	
                    	}
                        Methods.getPlot(p.getLocation()).clear(new Runnable() {
    						
    						@Override
    						public void run() {
    							p.sendMessage(Methods.getPrefix() + "§aTerreno limpo!");
		                    	/*
		                		Saldo.getInstance().setSaldo(p.getName(), (Cash.getCash(p.getName()) - 4.0));
		                		*/
    							Methods.remove(p.getName(), Main.get().getConfig().getInt("Economia.Preço_Limpar"));		
    							//JH_Shop.Main.getAPI().setPontos(p.getName(), (JH_Shop.Main.getAPI().getCachedCash(p.getName())) - Main.get().getConfig().getDouble("Economia.Preço_Limpar"));
    							/*
    							Methods.withdrawPlayer(p, Main.get().getConfig().getLong("Economia.Preço_Limpar_Pedreiro"));
    							*/
    						}
    					});
                	} else {
                		p.sendMessage(Methods.getPrefix() + "§cVocê não possui Saldo suficiente!");
                	}
                }
            }
        }
        if (e.getInventory().getTitle().startsWith("Você tem certeza? (Exclu") && e.getCurrentItem() != null) {
            final Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cCancelar")) {
                    Principal.open(p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aConfirmar")) {
                	Iterator<Pedreiro> iter = Main.ped.iterator();

                	while (iter.hasNext()) {
                	    Pedreiro str = iter.next();

                	    if (str.getPlayerName().equalsIgnoreCase(p.getName())){
                	        iter.remove();
                	        str.getBlocks().clear();
                	    }
                	    	
                	}
                    Methods.getPlot(p.getLocation()).deletePlot(new Runnable() {
						public void run() {
							p.sendMessage(Methods.getPrefix() + "§aTerreno excluido!");
							p.closeInventory();
						}
					});
                }
            }
        }
        if (e.getInventory().getTitle().startsWith("Menu dos terr") && e.getCurrentItem() != null) {
            final Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Voltar")) {
                    Principal.getPanel(p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eMembros")) {
                    Membros.open(p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eExpulsar membro")) {
                    Expulsar.open(p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eExcluir terreno")) {
                    Confirmações.openDispose(p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eLimpar terreno")) {
                	Confirmações.openClear(p);
                	/*
                    Methods.getPlot(p.getLocation()).clear(new Runnable() {
						
						@Override
						public void run() {
							p.sendMessage(Methods.getPrefix() + "§aTerreno limpo!");
						}
					});
					*/
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§ePedreiro")) {
                	for (Pedreiro check : Main.ped){
                		if (check.getPlayerName().equalsIgnoreCase(p.getName())){
                			p.sendMessage(Methods.getPrefix() + "§cVocê já está limpando um terreno! Aguarde.");
                			return;
                		}
                		if (!Methods.getPlot(p.getLocation()).isOwner(p.getUniqueId())){
                			p.sendMessage(Methods.getPrefix() + "§cVocê não é dono desse terreno.");
                			return;
                		}
                	}
                	if (Methods.get(p.getName()) >= Main.get().getConfig().getDouble("Economia.Preço_Limpar_Pedreiro")){
                			
                		Pedreiro pe = new Pedreiro(p);
                    	new BukkitRunnable() {
						
							@Override
							public void run() {
		                    	pe.start();
		                    	Main.ped.add(pe);
		                    	/*
		                		Saldo.getInstance().setSaldo(p.getName(), (Cash.getCash(p.getName()) - 4.0));
		                		*/
		                    	/*
		                    	Methods.withdrawPlayer(p, Main.get().getConfig().getDouble("Economia.Preço_Limpar"));
		                    	*/
		                    	Methods.remove(p.getName(), Main.get().getConfig().getInt("Economia.Preço_Limpar_Pedreiro"));
		                    	//JH_Shop.Main.getAPI().setPontos(p.getName(), (JH_Shop.Main.getAPI().getCachedCash(p.getName())) - Main.get().getConfig().getDouble("Economia.Preço_Limpar_Pedreiro"));
							}
						}.runTaskLater(Main.get(), 20L);
                	} else {
                		p.sendMessage(Methods.getPrefix() + "§cVocê não possui Saldo suficiente!");
                	}
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eConfiar")) {
                	AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
                        
                        @Override
                        public void onAnvilClick(AnvilClickEvent event) {
                            if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                                event.setWillClose(true);
                                event.setWillDestroy(true);
                                if (!(UUIDHandler.getUUIDFromString(event.getName()) == null)){
                                	if (!Methods.getPlot(p.getLocation()).getTrusted().contains(UUIDHandler.getUUIDFromString(event.getName()))){
                                		Methods.getPlot(p.getLocation()).addTrusted(UUIDHandler.getUUIDFromString(event.getName()));
                                		p.sendMessage(Methods.getPrefix() + "§aUsuário confiado!");
                                		new BukkitRunnable() {
											
											@Override
											public void run() {
												Principal.open(p);
											}
										}.runTaskLater(Main.get(), 5L);
                                	} else {
                                		p.sendMessage(Methods.getPrefix() + "§cO usuário já faz parte dessa categoria!");
                                	}
								} else {
									p.sendMessage(Methods.getPrefix() + "§cNão foi possível confiar esse jogador. Tente novamente.");
								}
                                
                            }else{
                                event.setWillClose(false);
                                event.setWillDestroy(false);
                            }
                        }
                    });
                 
                    ItemStack is = new ItemStack(Material.NAME_TAG);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName("Digite o usuário");
                    is.setItemMeta(im);
                 
                    gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, is);
                 
                    try {
						gui.open();
					} catch (IllegalAccessException | InvocationTargetException | InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eNegar")) {
                	AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
                        
                        @Override
                        public void onAnvilClick(AnvilClickEvent event) {
                            if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                                event.setWillClose(true);
                                event.setWillDestroy(true);
                                if (!(UUIDHandler.getUUIDFromString(event.getName()) == null)){
                                  	if (!Methods.getPlot(p.getLocation()).isDenied(UUIDHandler.getUUIDFromString(event.getName()))){
                                		Methods.getPlot(p.getLocation()).addDenied(UUIDHandler.getUUIDFromString(event.getName()));
                                		p.sendMessage(Methods.getPrefix() + "§aUsuário negado!");
                                		new BukkitRunnable() {
											
											@Override
											public void run() {
												Principal.open(p);
											}
										}.runTaskLater(Main.get(), 5L);
                                	} else {
                                		p.sendMessage(Methods.getPrefix() + "§cO usuário já faz parte dessa categoria!");
                                	}
								} else {
									p.sendMessage(Methods.getPrefix() + "§cNão foi possível negar esse jogador. Tente novamente.");
								}
                                
                            }else{
                                event.setWillClose(false);
                                event.setWillDestroy(false);
                            }
                        }
                    });
                 
                    ItemStack is = new ItemStack(Material.NAME_TAG);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName("Digite o usuário");
                    is.setItemMeta(im);
                 
                    gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, is);
                 
                    try {
						gui.open();
					} catch (IllegalAccessException | InvocationTargetException | InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eAdicionar")) {
                	AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
                        
                        @Override
                        public void onAnvilClick(AnvilClickEvent event) {
                            if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                                event.setWillClose(true);
                                event.setWillDestroy(true);
                                if (!(UUIDHandler.getUUIDFromString(event.getName()) == null)){
                                  	if (!Methods.getPlot(p.getLocation()).getMembers().contains(UUIDHandler.getUUIDFromString(event.getName()))){
                                		Methods.getPlot(p.getLocation()).addMember(UUIDHandler.getUUIDFromString(event.getName()));
                                		p.sendMessage(Methods.getPrefix() + "§aUsuário adicionado!");
                                		new BukkitRunnable() {
											
											@Override
											public void run() {
												Principal.open(p);
											}
										}.runTaskLater(Main.get(), 5L);
                                	} else {
                                		p.sendMessage(Methods.getPrefix() + "§cO usuário já faz parte dessa categoria!");
                                	}
								} else {
									p.sendMessage(Methods.getPrefix() + "§cNão foi possível adicionar esse jogador. Tente novamente.");
								}
                                
                            }else{
                                event.setWillClose(false);
                                event.setWillDestroy(false);
                            }
                        }
                    });
                 
                    ItemStack is = new ItemStack(Material.NAME_TAG);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName("Digite o usuário");
                    is.setItemMeta(im);
                 
                    gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, is);
                 
                    try {
						gui.open();
					} catch (IllegalAccessException | InvocationTargetException | InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eRemover")) {
                	AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
                        
                        @Override
                        public void onAnvilClick(AnvilClickEvent event) {
                            if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                                event.setWillClose(true);
                                event.setWillDestroy(true);
                                if (!(UUIDHandler.getUUIDFromString(event.getName()) == null)){
                                  	if (Methods.getPlot(p.getLocation()).isDenied(UUIDHandler.getUUIDFromString(event.getName())) || Methods.getPlot(p.getLocation()).isAdded(UUIDHandler.getUUIDFromString(event.getName()))){
                                    	Methods.getPlot(p.getLocation()).removeDenied(UUIDHandler.getUUIDFromString(event.getName()));
                                    	Methods.getPlot(p.getLocation()).removeMember(UUIDHandler.getUUIDFromString(event.getName()));
                                    	Methods.getPlot(p.getLocation()).removeTrusted(UUIDHandler.getUUIDFromString(event.getName()));
                                		p.sendMessage(Methods.getPrefix() + "§aUsuário removido!");
                                		new BukkitRunnable() {
											
											@Override
											public void run() {
												Principal.open(p);
											}
										}.runTaskLater(Main.get(), 5L);
                                	} else {
                                		p.sendMessage(Methods.getPrefix() + "§cO usuário não tem nenhuma relação a ser removida com o terreno.");
                                	}
								} else {
									p.sendMessage(Methods.getPrefix() + "§cNão foi possível remover esse jogador. Tente novamente.");
								}
                                
                            }else{
                                event.setWillClose(false);
                                event.setWillDestroy(false);
                            }
                        }
                    });
                 
                    ItemStack is = new ItemStack(Material.NAME_TAG);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName("Digite o usuário");
                    is.setItemMeta(im);
                 
                    gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, is);
                 
                    try {
						gui.open();
					} catch (IllegalAccessException | InvocationTargetException | InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eVender terreno")) {
            	AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
                    
                    @Override
                    public void onAnvilClick(AnvilClickEvent event) {
                        if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                            event.setWillClose(true);
                            event.setWillDestroy(true);
							try {
								if (!(Methods.getPlotAPI().getPlot(p.getLocation()) == null))
									if (Methods.getPlotAPI().getPlot(p.getLocation()).isOwner(p.getUniqueId()))
										if (Corretor.terrains.stream()
												.filter(c -> Methods.getPlot(new Location(Bukkit.getWorld(c.getWorld()), c.getX(), c.getY(), c.getZ()))
														.equals(Methods.getPlot(p.getLocation())))
												.findFirst().orElse(null) == null)
											if (!Methods.getPlot(p.getLocation()).isMerged())
												Corretor.venderTerreno(p, Integer.parseInt(event.getName()));
											else
												p.sendMessage(Methods.getPrefix() + "§cVocê não pode vender plots agrupados.");
										else
											p.sendMessage(Methods.getPrefix() + "§cEsse terreno já está a venda.");
									else
										p.sendMessage(Methods.getPrefix() + "§cEsse plot não é seu.");
								else
									p.sendMessage(Methods.getPrefix() + "§cVocê não está em um plot.");
							} catch (NumberFormatException e) {
								p.sendMessage(Methods.getPrefix() + "§7Por favor, indique o valor em números");
							} catch (NullPointerException e2) {
								p.sendMessage(Methods.getPrefix() + "§7Por favor, indique o valor em números");
							}
                            
                        }else{
                            event.setWillClose(false);
                            event.setWillDestroy(false);
                        }
                    }
                });
             
                @SuppressWarnings("deprecation")
				ItemStack is = new ItemStack(Material.getMaterial(175));
                ItemMeta im = is.getItemMeta();
                im.setDisplayName("Digite o valor");
                is.setItemMeta(im);
             
                gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, is);
             
                try {
					gui.open();
				} catch (IllegalAccessException | InvocationTargetException | InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        }
        if (e.getInventory().getTitle().startsWith("Terrenos: Mem") && e.getCurrentItem() != null) {
            final Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eConfiados")) {
                    Trusted.open(p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eMembros")) {
                    Members.open(p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eNegados")) {
                    Denied.open(p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Voltar")) {
                    Principal.open(p);
                }
            }
        }
        if (e.getInventory().getTitle().startsWith("Usuários confiad") || e.getInventory().getTitle().startsWith("Usuários negad") || e.getInventory().getTitle().startsWith("Membros do terre") && e.getCurrentItem() != null) {
            final Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Voltar")) {
                    Membros.open(p);
                }
            }
        }
        if (e.getInventory().getTitle().startsWith("Terrenos a venda: ") && e.getCurrentItem() != null) {
            final Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aPr\u00f3xima p\u00e1gina")) {
                	CorretorInv.getCorretor(p, CorretorInv.page_opened.get(p.getName()) + 1);
                    return;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aP\u00e1gina anterior")) {
                	CorretorInv.getCorretor(p, CorretorInv.page_opened.get(p.getName()) - 1);
                    return;
                }
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
                if (e.getClick() == ClickType.SHIFT_LEFT)
                	if (e.getCurrentItem().getItemMeta().getDisplayName().split("§f")[1].equals(p.getName()))
                		if (Corretor.terrains.stream().filter(c -> Methods.getPlot(new Location(Bukkit.getWorld(c.getWorld()), c.getX(), c.getY(), c.getZ())).equals(Methods.getPlot(p.getLocation()))).findFirst().orElse(null) != null)
                			Corretor.terrains.stream()
							.filter(c -> Methods.getPlot(new Location(Bukkit.getWorld(c.getWorld()), c.getX(), c.getY(), c.getZ()))
									.equals(Methods.getPlot(p.getLocation())))
							.findFirst().get().destroy(p, true);
                		else
                			p.sendMessage(Methods.getPrefix() + "§cVocê deve estar dentro do terreno para cancelar a venda do mesmo.");
                	else
                		CorretorInv.getOptionsCorretor(p, e.getCurrentItem());
                else
                	CorretorInv.getOptionsCorretor(p, e.getCurrentItem());
            }
        }
        if (e.getInventory().getTitle().startsWith("Terreno a venda: ") && e.getCurrentItem() != null) {
            final Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                final Plot terrain = CorretorInv.imovel.get(p.getName());
                
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eIr at\u00e9 o terreno")) {
                    Corretor.teleportRegionCorretor(terrain, p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eComprar o terreno")) {
            		if (terrain.isOwner(p.getUniqueId()))
            			p.sendMessage(Methods.getPrefix() + "§cVocê não pode comprar seu próprio terreno.");
            		else
            			Corretor.comprarTerreno(p, terrain, Integer.valueOf(NbtApi.getTag(e.getCurrentItem(),"preço")));
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Voltar")) {
                	CorretorInv.getCorretor(p, CorretorInv.page_opened.get(p.getName()));
                }
            }
        }
        if (e.getInventory().getTitle().startsWith("Obter terren") && e.getCurrentItem() != null) {
            final Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Voltar")) {
                    Principal.getPanel(p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eRandômicamente")) {
                	if (e.getClick() == ClickType.RIGHT){
                		PreBuilds.openInv(p);
                	} else {
                		if (Methods.getPlotPlayer(p).getPlotCount() + 1 > Methods.getLimit(p)){
                			p.sendMessage(Methods.getPrefix() + "§cLimite de terreno excedido! (Seu limite é: " + Methods.getLimit(p) + ")");
                			p.sendMessage(Methods.getPrefix() + "§cAdquira já seu VIP em nossa loja para expandir seu limite de terrenos.");
                			return;
                		}
                		p.performCommand("plot auto");
                	}
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eEste terreno")) {
                    if (Methods.getPlot(p.getLocation()) != null){
                    	if (Methods.getPlotPlayer(p).getPlotCount() + 1 > Methods.getLimit(p)){
                    		p.sendMessage(Methods.getPrefix() + "§cLimite de terreno excedido! (Seu limite é: " + Methods.getLimit(p) + ")");
                    		p.sendMessage(Methods.getPrefix() + "§cAdquira já seu VIP em nossa loja para expandir seu limite de terrenos.");
                    		return;
                    	}
                    	if (Methods.getPlot(p.getLocation()).canClaim(BukkitUtil.getPlayer(p))){
                    		//Methods.getPlot(p.getLocation()).claim(BukkitUtil.getPlayer(p), false, null);
                        	if (e.getClick() == ClickType.RIGHT)
                        		PreBuilds.openInv2(p);
                        	else
                        		p.performCommand("plot claim");
                    	} else {
                    		p.sendMessage(Methods.getPrefix() + "§cVocê não pode adquirir esse terreno.");
                    	}
                    } else {
                    	p.sendMessage(Methods.getPrefix() + "§cVocê não está em um terreno");
                    }
                }
            }
        }
        if (e.getInventory().getTitle().startsWith("Expulsar membros") && e.getCurrentItem() != null) {
            final Player p = (Player)e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Voltar")) {
                    Principal.open(p);
                    return;
                }
                for(String s : e.getCurrentItem().getItemMeta().getLore()){
                    if(ChatColor.stripColor(s).contains("expulsar")){
                    	Player alvo = Bukkit.getPlayerExact(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    	Methods.getPlot(p.getLocation()).getPlayersInPlot().contains(BukkitUtil.getPlayer(alvo));
                    	if (Methods.getPlot(p.getLocation()).getPlayersInPlot().contains(BukkitUtil.getPlayer(alvo))){
                       		alvo.teleport(alvo.getWorld().getSpawnLocation());
                    		p.sendMessage(Methods.getPrefix() + "§aPlayer expulso!");
                    		Expulsar.open(p);
                    	} else {
                    		p.sendMessage(Methods.getPrefix() + "§cO player não está em seu terreno.");
                    		Expulsar.open(p);
                    	}
                    	break;
                    }
                }
            }
        }
    }
	
	private static String[] comandos = new String[] { "/plots", "/p", "/plot", "/ps", "/plotsquared", "/p2", "/2", "/plotme" };
	
	private static boolean hasArgs(String command){
		return command.contains(" ");
	}
	
	@EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		for (String msg : comandos){
			if (event.getMessage().startsWith(msg)){
				if (!event.getPlayer().isOp()){
					if (hasArgs(event.getMessage())){

					} else if (event.getMessage().equalsIgnoreCase(msg)){
						event.setCancelled(true);
						event.getPlayer().performCommand("terreno");
					}
				}
			}
		}
    }
	
//	@EventHandler
//    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
//		for (String msg : comandos){
//			if (event.getMessage().startsWith(msg)){
//				if (!event.getPlayer().isOp()){
//					if (hasArgs(event.getMessage())){
//						String comando = event.getMessage().split(" ")[0];
//						if (comando.equalsIgnoreCase(msg)){
//							event.setCancelled(true);
//							event.getPlayer().performCommand("terreno");
//						}
//					} else if (event.getMessage().equalsIgnoreCase(msg)){
//						event.setCancelled(true);
//						event.getPlayer().performCommand("terreno");
//					}
//				}
//			}
//		}
//    }

}
