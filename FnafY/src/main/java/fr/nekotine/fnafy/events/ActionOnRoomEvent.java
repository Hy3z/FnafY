package fr.nekotine.fnafy.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;

import doorRoom.Room;
import team.Team;

public class ActionOnRoomEvent extends Event{
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	private final Player player;
	private final Room room;
	private final Action action;
	private final Team team;
	public ActionOnRoomEvent(Player player, Room room, Action action, Team team) {
		this.player=player;
		this.room=room;
		this.action=action;
		this.team=team;
	}
	public Room getRoom() {
		return room;
	}
	public Player getPlayer() {
		return player;
	}
	public Action getAction() {
		return action;
	}
	public Team getTeam() {
		return team;
	}
	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
}
