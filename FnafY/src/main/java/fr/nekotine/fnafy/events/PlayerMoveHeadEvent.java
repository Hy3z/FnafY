package fr.nekotine.fnafy.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerMoveHeadEvent extends Event implements Cancellable {
	private boolean isCancelled;
	private final Player player;
	private final Location before;
	private final Location after;
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	public PlayerMoveHeadEvent(Player player, Location before, Location after) {
		this.player=player;
		this.before=before;
		this.after=after;
		this.isCancelled=false;
	}
	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
	public Location getBefore() {
		return before;
	}
	public Location getAfter() {
		return after;
	}
	@Override
	public boolean isCancelled() {
		return isCancelled;
	}
	public Player getPlayer() {
		return player;
	}
	@Override
	public void setCancelled(boolean cancel) {
		isCancelled=cancel;
	}
	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
}
