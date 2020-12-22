package fr.nekotine.fnafy.room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.doors.Door;

public class RoomManager{
	private final FnafYMain main;
	private final HashMap<String, Room> rooms = new HashMap<>();
	public RoomManager(FnafYMain main) {
		this.main=main;
	}
	public void setRoomHash(HashMap<String, Room> rooms){
		this.rooms.clear();
		this.rooms.putAll(rooms);
	}

	public Room getRoom(String roomName) {
		return rooms.get(roomName);
	}
	public HashMap<String, Room> getRooms(){
		return rooms;
	}
	public List<Room> canMoveFromList(Room r){
		List<Room> canMoveFromTo = new ArrayList<>();
		for(Door d : main.getDoorManager().getAllDoors()) {
			Room newRoom = d.canMoveTo(r);
			if(newRoom!=null) {
				canMoveFromTo.add(newRoom);
			}
		}
		return canMoveFromTo;
	}
	public List<Room> cannotMoveFromList(Room r){
		List<Room> cannotMoveFromTo = new ArrayList<>();
		List<Room> canMoveFromTo = canMoveFromList(r);
		for(Room room : rooms.values()) {
			if(!canMoveFromTo.contains(room)) {
				cannotMoveFromTo.add(room);
			}
		}
		return cannotMoveFromTo;
	}
	public boolean canMoveFromToBool(Room prev, Room next) {
		for(Door d : main.getDoorManager().getAllDoors()) {
			if(d.canMoveFromToBoolean(prev, next)) {
				return true;
			}
		}
		return false;
	}
}