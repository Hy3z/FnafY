package fr.nekotine.fnafy.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import doorRoom.Room;
import team.Team;

public class LookAtRoomEvent extends Event{
	private final Player player;
	private final Room room;
	private final Team team;
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	public LookAtRoomEvent(Player player, Room room, Team team) {
		this.player=player;
		this.room=room;
		this.team=team;
	}
	public Player getPlayer() {
		return player;
	}
	public Room getRoom() {
		return room;
	}
	public Team getTeam() {
		return team;
	}
	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
}
