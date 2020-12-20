package fr.nekotine.fnafy.room;

import java.util.HashMap;

import fr.nekotine.fnafy.FnafYMain;

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
}