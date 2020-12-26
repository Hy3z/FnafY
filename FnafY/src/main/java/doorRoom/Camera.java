package doorRoom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.events.PlayerMoveHeadEvent;
import team.Team;

public class Camera implements Listener{
	private final Player player;
	private final Team team;
	private final FnafYMain main;
	public Camera(FnafYMain main, Location camLoc, Player player, Team team) {
		this.player = player;
		this.main = main;
		this.team=team;
		player.teleport(camLoc);
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void playerMoveHead(PlayerMoveHeadEvent e) {
		if(e.getPlayer().equals(player)) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void crouchEvent(PlayerToggleSneakEvent e) {
		if(e.getPlayer().equals(player)) {
			e.setCancelled(true);
			if(team==Team.AFTON) {
				player.teleport(main.getAftonMinimapManager().getCameraBaseLocation());
			}else {
				player.teleport(main.getGuardMinimapManager().getCameraBaseLocation());
			}
			main.getHeadListener().trackPlayer(player);
			HandlerList.unregisterAll(this);
		}
	}
}
