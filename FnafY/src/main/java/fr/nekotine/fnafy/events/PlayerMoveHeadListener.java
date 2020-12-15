package fr.nekotine.fnafy.events;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import fr.nekotine.fnafy.FnafYMain;

public abstract class PlayerMoveHeadListener extends JavaPlugin implements Listener{
	private final FnafYMain main;
	private final long REFRESHTICKTIMER=20;
	private HashMap<Player, Location> eyeLocationPerPlayer = new HashMap<>();
	public PlayerMoveHeadListener(FnafYMain main) {
		this.main=main;
		scheduleEvent();
	}
	public abstract void playerMoveHeadEvent(Player p);
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent e) {
		if(eyeLocationPerPlayer.containsKey(e.getPlayer())) {
			eyeLocationPerPlayer.remove(e.getPlayer());
		}
	}
	private void scheduleEvent() {
		BukkitScheduler scheduler = main.getServer().getScheduler();
	    scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
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
	public boolean untrackPlayer(Player p) {
		if(eyeLocationPerPlayer.containsKey(p)) {
			eyeLocationPerPlayer.remove(p);
			return true;
		}
		return false;
	}
}
