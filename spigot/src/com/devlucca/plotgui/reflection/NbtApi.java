package com.devlucca.plotgui.reflection;

import org.bukkit.inventory.ItemStack;

import com.devlucca.plotgui.reflection.NbtFactory.NbtCompound;

public class NbtApi {
	
    public static ItemStack setNBTData(ItemStack item,String tag, String data){
    	ItemStack stack = NbtFactory.getCraftItemStack(item);
    	NbtCompound compound = NbtFactory.fromItemTag(stack);
    	compound.putPath(tag, data);
        return stack;
    }

    public static boolean hasTag(ItemStack item, String tag){
    	ItemStack stack = NbtFactory.getCraftItemStack(item);
    	NbtCompound compound = NbtFactory.fromItemTag(stack);
        if(compound.getPath(tag) != null) return true;
        return false;
    }

    public static String getTag(ItemStack item, String tag){
    	ItemStack stack = NbtFactory.getCraftItemStack(item);
    	NbtCompound compound = NbtFactory.fromItemTag(stack);
        return compound.getPath(tag);
    }

}