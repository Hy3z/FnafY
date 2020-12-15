package fr.nekotine.fnafy.room;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.events.PlayerMoveHeadListener;

public class RoomMinimapManager extends PlayerMoveHeadListener implements Listener{
	private final List<RoomMinimap> roomMinimapList = new ArrayList<>();
	public RoomMinimapManager(FnafYMain main, List<RoomMinimap> roomMinimapList) {
		super(main);
		this.roomMinimapList.addAll(roomMinimapList);
	}
	@Override
	public void playerMoveHeadEvent(Player p) {
		for(RoomMinimap r : roomMinimapList) {
			r.playerMoveHeadEvent(p);
		}
	}
	public boolean trackPlayer(Player p) {
		return super.trackPlayer(p);
	}
	public boolean untrackPlayer(Player p) {
		return super.untrackPlayer(p);
	}
}
