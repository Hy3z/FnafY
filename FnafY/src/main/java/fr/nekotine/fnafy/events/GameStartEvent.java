package fr.nekotine.fnafy.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}
	@Override
	public void setCancelled(boolean cancel) {
		this.cancel=cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
