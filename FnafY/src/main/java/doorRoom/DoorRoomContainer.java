package doorRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import animatronic.Animatronic;
import fr.nekotine.fnafy.animation.ASAnimation;

public class DoorRoomContainer {
	private final HashMap<String, Room> rooms = new HashMap<>();
	private final HashMap<String, Door> doors = new HashMap<>();
	public DoorRoomContainer(HashMap<String, Room> rooms, HashMap<String, Door> doors) {
		this.rooms.putAll(rooms);
		this.doors.putAll(doors);
	}
	public Door getDoor(String doorName) {
		return doors.get(doorName);
	}
	public Room getRoom(String roomName) {
		return rooms.get(roomName);
	}
	public HashMap<String, Room> getRooms(){
		return rooms;
	}
	public List<Room> canMoveFromList(Room r){
		List<Room> canMoveFromTo = new ArrayList<>();
		for(Door d : doors.values()) {
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
		for(Door d : doors.values()) {
			if(d.canMoveFromToBoolean(prev, next)) {
				return true;
			}
		}
		return false;
	}
	public List<Door> canMoveFromToDoorList(Room prev, Room next) {
		List<Door> doorList = new ArrayList<>();
		for(Door d : doors.values()) {
			if(d.canMoveFromToBoolean(prev, next)) {
				doorList.add(d);
			}
		}
		return doorList;
	}
	public List<ASAnimation> getDoorAnimationFromRoomToRoom(Room prev, Room next, Animatronic anim){
		List<Door> doorList = canMoveFromToDoorList(prev, next);
		if(doorList.size()>0) {
			return doorList.get((int)Math.random()*doorList.size()).getDoorAnimationsToRoom(next, anim);
		}
		return null;
	}
	public List<ASAnimation> getDoorMinimapAnimationFromRoomToRoom(Room prev, Room next, Animatronic anim){
		List<Door> doorList = canMoveFromToDoorList(prev, next);
		if(doorList.size()>0) {
			return doorList.get((int)Math.random()*doorList.size()).getMinimapDoorAnimationsToRoom(next, anim);
		}
		return null;
	}
}
