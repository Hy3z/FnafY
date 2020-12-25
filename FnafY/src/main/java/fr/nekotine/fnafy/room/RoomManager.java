package fr.nekotine.fnafy.room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.doors.Door;
import fr.nekotine.fnafy.enums.Animatronic;

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
	public List<Door> canMoveFromToDoorList(Room prev, Room next) {
		List<Door> doorList = new ArrayList<>();
		for(Door d : main.getDoorManager().getAllDoors()) {
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