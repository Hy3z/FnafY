package fr.nekotine.fnafy.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.room.Room;

public class AnimatronicEnterRoomEvent extends Event{
	private final Room room;
	private final Animatronic anim;
	public AnimatronicEnterRoomEvent(Room room, Animatronic anim) {
		this.room = room;
		this.anim = anim;
		
	}
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
	public Room getRoom() {
		return room;
	}
	public Animatronic getAnim() {
		return anim;
	}

}
