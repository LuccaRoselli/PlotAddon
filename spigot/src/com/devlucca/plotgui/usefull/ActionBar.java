package com.devlucca.plotgui.usefull;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class ActionBar {
	
    public static void send(final Player p, final String text) {
        final PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte)2);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }
    
    public static void sendAll(final String text) {
        final PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte)2);
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        }
    }

}

