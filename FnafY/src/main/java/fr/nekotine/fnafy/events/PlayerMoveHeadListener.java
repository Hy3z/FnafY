package fr.nekotine.fnafy.events;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.nekotine.fnafy.FnafYMain;

public abstract class PlayerMoveHeadListener implements Listener{
	private final FnafYMain main;
	private final long REFRESHTICKTIMER=20;
	private HashMap<Player, Location> eyeLocationPerPlayer = new HashMap<>();
	private int scheduler;
	private boolean schedulerRunning = false;
	public PlayerMoveHeadListener(FnafYMain main) {
		this.main=main;
	}
	public abstract void playerMoveHeadEvent(Player p);
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent e) {
		if(eyeLocationPerPlayer.containsKey(e.getPlayer())) {
			eyeLocationPerPlayer.remove(e.getPlayer());
		}
	}
	public boolean isScheduling() {
		return schedulerRunning;
	}
	public boolean triggerSchedule() {
		if(schedulerRunning) {
			main.getServer().getScheduler().cancelTask(scheduler);
			return false;
		}
		scheduleEvent();
		return true;
	}
	private void scheduleEvent() {
		scheduler = main.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
	        @Override
	        public void run() {
	        	for(Player p : eyeLocationPerPlayer.keySet()) {
	        		if(p.getEyeLocation().getYaw()!=eyeLocationPerPlayer.get(p).getYaw() || 
	        		p.getEyeLocation().getPitch()!=eyeLocationPerPlayer.get(p).getPitch()) {
	        			eyeLocationPerPlayer.replace(p, p.getEyeLocation());
	        			playerMoveHeadEvent(p);
	        		}
	        	}
	        }
	    }, 0, REFRESHTICKTIMER);
	}
	public boolean trackPlayer(Player p) {
		if(!eyeLocationPerPlayer.containsKey(p)) {
			eyeLocationPerPlayer.put(p, p.getEyeLocation());
			return true;
		}
		return false;
	}
	public PlayerMoveHeadListener getSuper() {
		return this;
	}
	public boolean untrackPlayer(Player p) {
		if(eyeLocationPerPlayer.containsKey(p)) {
			eyeLocationPerPlayer.remove(p);
			return true;
		}
		return false;
	}
}
