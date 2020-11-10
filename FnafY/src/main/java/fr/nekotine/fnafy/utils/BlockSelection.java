package fr.nekotine.fnafy.utils;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class BlockSelection{
	
	public static final BlockData OUTLINE_ON = Bukkit.createBlockData(Material.EMERALD_BLOCK);
	
	private final Location baseLoc;
	private final LinkedList<BlockSelectionPart> parts = new LinkedList<BlockSelectionPart>();
	
	private BlockSelection(Location baseloc) {
		baseLoc = baseloc;
	}

	public void outline(Player p) {
		Location loc = baseLoc.clone();
		for (BlockSelectionPart pa : parts) {
			pa.apply(loc);
			p.sendBlockChange(loc,OUTLINE_ON);
		}
	}
	
	public void disOutline(Player p) {
		Location loc = baseLoc.clone();
		for (BlockSelectionPart pa : parts) {
			pa.apply(loc);
			p.sendBlockChange(loc,loc.getBlock().getBlockData());
		}
	}
	
	public boolean isOneSelected(Location loc) {
		Block locb=loc.getBlock();
		Location loca = baseLoc.clone();
		for (BlockSelectionPart pa : parts) {
			pa.apply(loc);
			if (loca.getBlock().equals(locb)) return true;
		}
		return false;
	}
	
}
