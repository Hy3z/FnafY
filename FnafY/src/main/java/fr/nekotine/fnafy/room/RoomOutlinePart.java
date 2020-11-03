package fr.nekotine.fnafy.room;

import org.bukkit.Location;

public class RoomOutlinePart {
	
	private final int x;
	private final int y;
	private final int z;
	
	public RoomOutlinePart(int x,int y,int z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
	
	public void apply(Location loc) {
		loc.add(x, y, z);
	}
}
