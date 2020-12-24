package fr.nekotine.fnafy.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameTickEvent extends Event{
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
}
