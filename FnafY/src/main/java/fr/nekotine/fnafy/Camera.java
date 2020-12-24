package fr.nekotine.fnafy;

import org.bukkit.entity.Player;

import fr.nekotine.fnafy.room.Room;

public class Camera {
	private final Player player;
	private final Room room;
	public Camera(Room room, Player player) {
		this.player = player;
		this.room = room;
	}
	public Player getPlayer() {
		return player;
	}
	public Room getRoom() {
		return room;
	}
	
}
