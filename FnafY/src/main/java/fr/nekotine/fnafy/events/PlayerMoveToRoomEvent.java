package fr.nekotine.fnafy.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.nekotine.fnafy.doorRoom.Room;

public class PlayerMoveToRoomEvent extends Event{
	private final Player player;
	private final Room roomFrom;
	private final Room goingTo;
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	public PlayerMoveToRoomEvent(Room roomFrom, Player player, Room goingTo) {
		this.player = player;
		this.roomFrom = roomFrom;
		this.goingTo = goingTo;
	}
	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
	public Player getPlayer() {
		return player;
	}
	public Room getRoomFrom() {
		return roomFrom;
	}
	public Room getGoingTo() {
		return goingTo;
	}
	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
}
