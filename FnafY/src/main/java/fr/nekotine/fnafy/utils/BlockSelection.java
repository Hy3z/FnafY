package fr.nekotine.fnafy.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
@SerializableAs("BlockSelection")
public class BlockSelection implements ConfigurationSerializable{
	
	public static final BlockData OUTLINE_ON = Bukkit.createBlockData(Material.EMERALD_BLOCK);
	
	private final Location baseLoc;
	private final LinkedList<BlockSelectionPart> parts = new LinkedList<BlockSelectionPart>();
	
	public BlockSelection(Location baseloc, LinkedList<BlockSelectionPart> parts) {
		baseLoc = baseloc;
		this.parts.addAll(parts);
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
	
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> serialized = new HashMap<String, Object>();
		serialized.put("baseLoc", this.baseLoc);
		serialized.put("length", this.parts.size());
		for(int x=1;x<=this.parts.size();x++) {
			serialized.put(Integer.toString(x), parts.get(x-1));
		}
		return serialized;
	}
	 public static BlockSelection deserialize(Map<String, Object> args) {
		 Location baseLoc = (Location)args.get("baseLoc");
		 int length = (int)args.get("length");
		 LinkedList<BlockSelectionPart> parts = new LinkedList<>();
		 for(int x=1;x<=length;x++) {
			 parts.add((BlockSelectionPart)args.get(Integer.toString(x)));
		 }
		 return new BlockSelection(baseLoc, parts);
	 }
	 
}
