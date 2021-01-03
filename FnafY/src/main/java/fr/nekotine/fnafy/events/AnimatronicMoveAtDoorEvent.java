package fr.nekotine.fnafy.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.nekotine.fnafy.animatronic.Animatronic;
import fr.nekotine.fnafy.doorRoom.Door;
import fr.nekotine.fnafy.doorRoom.Room;

public class AnimatronicMoveAtDoorEvent extends Event{
	private final Door door;
	private final Room goingTo;
	private final Animatronic anim;
	public AnimatronicMoveAtDoorEvent(Door door, Animatronic anim, Room goingTo) {
		this.door = door;
		this.goingTo = goingTo;
		this.anim = anim;
	}
	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
	public Door getDoor() {
		return door;
	}
	public Animatronic getAnim() {
		return anim;
	}
	public Room getGoingTo() {
		return goingTo;
	}

}
