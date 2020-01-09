package com.devlucca.plotgui.usefull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.Bukkit;

import com.devlucca.plotgui.Main;
import com.intellectualcrafters.plot.object.Location;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;

public class Schematic {

	public static void paste(Location loc, boolean ignoreAirBlocks, String schematic, int x_fix, int y_fix, int z_fix)
			throws FileNotFoundException, IOException, MaxChangedBlocksException {;
		File file = new File(Main.get().getDataFolder(), schematic + ".schematic"); // The
																					// schematic
																					// file
		Vector to = new Vector(loc.getX() - x_fix, loc.getY() - y_fix, loc.getZ() - z_fix); // Where you
																	// want to
																	// paste

		World weWorld = new BukkitWorld(Bukkit.getWorld(loc.getWorld()));
		WorldData worldData = weWorld.getWorldData();
		Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(file)).read(worldData);
		EditSession extent = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
		AffineTransform transform = new AffineTransform();

		// { // Uncomment this if you want to rotate the schematic
		// transform = transform.rotateY(90); // E.g. rotate 90
		// extent = new BlockTransformExtent(clipboard, transform,
		// worldData.getBlockRegistry());
		// }

		ForwardExtentCopy copy = new ForwardExtentCopy(clipboard, clipboard.getRegion(), clipboard.getOrigin(), extent,
				to);
		if (!transform.isIdentity())
			copy.setTransform(transform);
		if (ignoreAirBlocks) {
			copy.setSourceMask(new ExistingBlockMask(clipboard));
		}
		Operations.completeLegacy(copy);
		extent.flushQueue();
	}
}
