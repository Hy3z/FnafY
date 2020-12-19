package fr.nekotine.fnafy.room;

import java.util.HashMap;

import org.bukkit.entity.Player;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.events.PlayerMoveHeadListener;

public class RoomManager extends PlayerMoveHeadListener{
	private final HashMap<String, Room> rooms = new HashMap<>();
	public RoomManager(FnafYMain main) {
		super(main);
	}
	@Override
	public void playerMoveHeadEvent(Player p) {
		for(Room r : rooms.values()) {
			r.playerMoveHeadEvent(p);
		}
	}
	public void setRoomHash( HashMap<String, Room> rooms){
		rooms.clear();
		this.rooms.putAll(rooms);
	}
	public boolean trackPlayer(Player p) {
		return super.trackPlayer(p);
	}
	public boolean untrackPlayer(Player p) {
		return super.untrackPlayer(p);
	}
	public Room getRoom(String roomName) {
		return rooms.get(roomName);
	}
	public HashMap<String, Room> getRooms(){
		return rooms;
	}
}