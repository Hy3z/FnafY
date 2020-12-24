package fr.nekotine.fnafy;

import org.bukkit.entity.Player;

import fr.nekotine.fnafy.room.Room;

public class Camera {
	private final Player player;
	private final Room room;
	private final int angle;
	public Camera(Room room, Player player, int angle) {
		this.player = player;
		this.room = room;
		this.angle = angle;
		
	}
	public Player getPlayer() {
		return player;
	}
	public Room getRoom() {
		return room;
	}
	public int getAngle() {
		return angle;
	}
}
