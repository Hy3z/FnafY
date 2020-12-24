package fr.nekotine.fnafy.events;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerMoveHeadListener implements Listener{
	private HashMap<Player, Location> eyeLocationPerPlayer = new HashMap<>();
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent e) {
		if(eyeLocationPerPlayer.containsKey(e.getPlayer())) {
			eyeLocationPerPlayer.remove(e.getPlayer());
		}
	}
	public Set<Player> getTrackedPlayers(){
		return eyeLocationPerPlayer.keySet();
	}
	@EventHandler
	public void onGameTick(GameTickEvent tick) {
		for(Player p : eyeLocationPerPlayer.keySet()) {
    		if(p.getEyeLocation().getYaw()!=eyeLocationPerPlayer.get(p).getYaw() || 
    		p.getEyeLocation().getPitch()!=eyeLocationPerPlayer.get(p).getPitch()) {
    			PlayerMoveHeadEvent e = new PlayerMoveHeadEvent(p, eyeLocationPerPlayer.get(p), p.getEyeLocation());
    			Bukkit.getPluginManager().callEvent(e);
    			if(e.isCancelled()) {
    				p.teleport(eyeLocationPerPlayer.get(p).subtract(0, 1, 0));
    				return;
    			}
    			eyeLocationPerPlayer.replace(p, p.getEyeLocation());
    		}
    	}
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
