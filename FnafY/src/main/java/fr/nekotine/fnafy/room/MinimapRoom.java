package fr.nekotine.fnafy.room;

import org.bukkit.Location;

public class MinimapRoom {
	private final RoomOutline outline;
	private final RoomOutline selectionSurface;
	
	public MinimapRoom(String name,RoomOutline outl,RoomOutline selSur) {
		outline=outl;
		selectionSurface = selSur;
	}

	public RoomOutline getOutline() {
		return outline;
	}

	public RoomOutline getSelectionSurface() {
		return selectionSurface;
	}
	
	public boolean isInTotalSelectionSurface(Location loc) {
		return outline.isOneSelected(loc) || selectionSurface.isOneSelected(loc);
	}
	
}
