package fr.nekotine.fnafy.doors;

import java.util.Collection;
import java.util.HashMap;

import fr.nekotine.fnafy.FnafYMain;

public class DoorManager {
	private FnafYMain main;
	private final HashMap<String, Door> doors = new HashMap<>();
	public DoorManager(FnafYMain main) {
		this.main=main;
	}
	public void setDoorHash(HashMap<String, Door> doors) {
		doors.clear();
		this.doors.putAll(doors);
	}
	public Collection<Door> getAllDoors(){
		return doors.values();
	}
}
