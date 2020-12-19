package fr.nekotine.fnafy.commands;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import fr.nekotine.fnafy.utils.BlockSelectionPart;

public class OutlineCreator implements Listener{
	private final ComMapper cm;
	private final String mapName;
	private final String roomName; 
	private final String outline;
	private final Player p;
	private Vector precedent;
	private Location baseLoc;
	private boolean tryingToStop = false;
	private LinkedList<BlockSelectionPart> parts = new LinkedList<>();
	public static final BlockData OUTLINE_ON = Bukkit.createBlockData(Material.EMERALD_BLOCK);
	public OutlineCreator(ComMapper cm, String mapName, String roomName, String outline, Player p) {
		this.cm=cm;
		this.mapName=mapName;
		this.roomName=roomName;
		this.outline=outline;
		this.p=p;
		p.sendMessage(ChatColor.GREEN+"OutlineCreator started!");
	}
	@EventHandler
	public void clickEvent(PlayerInteractEvent e) {
		if(e.getPlayer().equals(p)) {
			if(e.getMaterial()==Material.WOODEN_HOE) {
				if(tryingToStop) {
					tryingToStop=false;
				}
				if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
					e.setCancelled(true);
					Vector vec = e.getClickedBlock().getLocation().toVector();
					if(precedent!=null) {
						if(vec.distanceSquared(precedent)>0 && vec.distanceSquared(precedent)<=2) {
							Vector bVector = vec.clone().subtract(precedent);
							parts.add(new BlockSelectionPart(bVector.getBlockX(), bVector.getBlockY(), bVector.getBlockZ()));
							p.sendMessage(ChatColor.GREEN+"Vector added: "+vec.clone().subtract(precedent));
							precedent=vec;
							showOutline(e.getClickedBlock().getLocation());
							return;
						}
						p.sendMessage(ChatColor.RED+"Selectionez un bloc adjaceant au précédent");
						return;
					}
					precedent=vec;
					baseLoc=e.getClickedBlock().getLocation();
					p.sendMessage(ChatColor.GREEN+"BlockSelection starting on: "+vec);
					showOutline(baseLoc);
					return;
				}
				if(e.getAction()==Action.LEFT_CLICK_BLOCK || e.getAction()==Action.LEFT_CLICK_AIR) {
					e.setCancelled(true);
					if(parts.size()>0) {
						hideOutline();
						cm.registerOutline(mapName, roomName, outline, baseLoc, parts, p);
						p.sendMessage(ChatColor.GREEN+"Outline registered!");
						return;
					}
					p.sendMessage(ChatColor.RED+"BlockSelection was empty, so we got you out of OutlineCreator!");
					hideOutline();
					cm.stopOutlineCreator(p);
					return;
				}
			}
		}
	}
	@EventHandler
	public void dropItem(PlayerDropItemEvent e) {
		if(e.getPlayer().equals(p)) {
			if(e.getItemDrop().getItemStack().getType()==Material.WOODEN_HOE) {
				e.setCancelled(true);
				if(tryingToStop) {
					p.sendMessage(ChatColor.GREEN+"OutlineCreator has been stopped");
					hideOutline();
					cm.stopOutlineCreator(p);
					return;
				}
				tryingToStop=true;
				p.sendMessage(ChatColor.RED+"Trying to cancel BlockSelection! Drop again to stop");
			}
		}
	}
	public void showOutline(Location l) {
		BukkitScheduler scheduler = cm.getMain().getServer().getScheduler();
	    scheduler.scheduleSyncDelayedTask(cm.getMain(), new Runnable() {
	        @Override
	        public void run() {
	        	p.sendBlockChange(l, OUTLINE_ON);
	        }
	    }, 2);
	}
	public void hideOutline() {
		Location loc = baseLoc.clone();
		for(BlockSelectionPart bsp : parts) {
			p.sendBlockChange(loc,loc.getBlock().getBlockData());
			loc.add(bsp.toVector());
		}
	}
}
