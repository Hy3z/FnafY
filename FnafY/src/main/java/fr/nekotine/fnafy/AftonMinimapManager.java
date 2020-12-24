package fr.nekotine.fnafy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;

import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.enums.Team;
import fr.nekotine.fnafy.events.ActionOnRoomEvent;
import fr.nekotine.fnafy.events.LookAtRoomEvent;
import fr.nekotine.fnafy.events.UnlookAtRoomEvent;
import fr.nekotine.fnafy.room.Room;

public class AftonMinimapManager implements Listener{
	private final FnafYMain main;
	private List<Player> inCameraPlayers = new ArrayList<>();
	private static final BlockData OUTLINE_GREEN = Bukkit.createBlockData(Material.EMERALD_BLOCK);
	private static final BlockData OUTLINE_RED = Bukkit.createBlockData(Material.REDSTONE_BLOCK);
	private static final BlockData OUTLINE_GOLD = Bukkit.createBlockData(Material.GOLD_BLOCK);
	
	private ArmorStand bonnie;
	private ArmorStand freddy;
	private ArmorStand chica;
	private ArmorStand foxy;
	private ArmorStand mangle;
	private ArmorStand springtrap;
	public AftonMinimapManager(FnafYMain main) {
		this.main=main;
	}
	public Material getPlayerMaterialInHand(Player p) {
		return p.getInventory().getItemInMainHand().getType();
	}
	public boolean isCameraUnlocked(Room r) {
		return main.getTeamAfton().getUnlockedPackages().contains(r.getAftonCameraPackage());
	}
	public Room getRoomFromWool(Player p) {
		return main.getRoomManager().getRoom(main.getTeamAfton().getAnimatronicRoomLocationName(Animatronic.getFromWool(getPlayerMaterialInHand(p))));
	}
	public boolean canMoveFromTo(Room prev, Room next) {
		return main.getRoomManager().canMoveFromToBool(prev,next);
	}
	public void drawAftonOutline(Room r, Player p, BlockData outline) {
		r.drawAftonOutline(p, outline);
	}
	public boolean enterCamera(Player player) {
		if(inCameraPlayers.contains(player)) return false;
		inCameraPlayers.add(player);
		main.getHeadListener().trackPlayer(player);
		return true;
	}
	public boolean leaveCamera(Player player) {
		if(!inCameraPlayers.contains(player)) return false;
		inCameraPlayers.remove(player);
		main.getHeadListener().untrackPlayer(player);
		return true;
	}
	@EventHandler
	public void itemHeldEvent(PlayerItemHeldEvent e) {
		Material previousMat = e.getPlayer().getInventory().getItem(e.getPreviousSlot()).getType();
		Material newMat = e.getPlayer().getInventory().getItem(e.getNewSlot()).getType();
		if(previousMat!=newMat) {
			clearAllOutline(e.getPlayer());
			//unglow all anim
			if(!newMat.equals(Material.AIR)) {
				Room r = getRoomFromWool(e.getPlayer());
				drawAftonOutline(r, e.getPlayer(), OUTLINE_GOLD);
				//glow animatronic
				for(Room room : main.getRoomManager().cannotMoveFromList(r)) {
					drawAftonOutline(room, e.getPlayer(), OUTLINE_RED);
				}
			}
		}
	}
	private void clearAllOutline(Player p) {
		for(Room r : main.getRoomManager().getRooms().values()) {
			r.hideGuardOutline(p);
		}
	}
	@EventHandler
	public void unlookAtRoom(UnlookAtRoomEvent e) {
		if(e.getTeam()==Team.AFTON) {
			if(getPlayerMaterialInHand(e.getPlayer()).equals(Material.AIR)) {
				e.getRoom().hideGuardOutline(e.getPlayer());
			}else if(canMoveFromTo(getRoomFromWool(e.getPlayer()),e.getRoom())) {
				e.getRoom().hideGuardOutline(e.getPlayer());
			}
		}
	}
	@EventHandler
	public void onLookAtRoom(LookAtRoomEvent e) {
		if(e.getTeam()==Team.AFTON) {
			if(getPlayerMaterialInHand(e.getPlayer()).equals(Material.AIR)) {
				if(isCameraUnlocked(e.getRoom())) {
					drawAftonOutline(e.getRoom(), e.getPlayer(), OUTLINE_GREEN);
				}else {
					drawAftonOutline(e.getRoom(), e.getPlayer(), OUTLINE_RED);
				}
			}else if(canMoveFromTo(getRoomFromWool(e.getPlayer()),e.getRoom())) {
				drawAftonOutline(e.getRoom(), e.getPlayer(), OUTLINE_GREEN);
			}
		}
	}
	@EventHandler
	public void onPlayerActionRoom(ActionOnRoomEvent e) {
		if(e.getTeam()==Team.AFTON) {
			if(getPlayerMaterialInHand(e.getPlayer()).equals(Material.AIR)) {
				if(isCameraUnlocked(e.getRoom())) {
					//entrer dans la caméra
				}else {
					//message de refus
				}
			}else if(canMoveFromTo(getRoomFromWool(e.getPlayer()),e.getRoom())) {
				//deplacer animatronic
			}else {
				//message de refus
			}
		}
	}
	public boolean packetGlowPlayer(Player player,Player glowed) {
		com.comphenix.protocol.events.PacketContainer packet = main.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
	     packet.getIntegers().write(0, glowed.getEntityId()); //Set packet's entity id
	     com.comphenix.protocol.wrappers.WrappedDataWatcher watcher = new com.comphenix.protocol.wrappers.WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
	     com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer serializer = Registry.get(Byte.class); //Found this through google, needed for some stupid reason
	     watcher.setEntity(player); //Set the new data watcher's target
	     watcher.setObject(0, serializer, (byte) (0x40)); //Set status to glowing, found on protocol page
	     packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
	     try {
	    	 main.getProtocolManager().sendServerPacket(player, packet);
	         return true;
	     } catch (InvocationTargetException e) {
	         e.printStackTrace();
	         return false;
	     }
	     //MERCI INTERNET PUTAIN
	}
	public boolean packetUnGlowPlayer(Player player,Player glowed) {
		com.comphenix.protocol.events.PacketContainer packet = main.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
	     packet.getIntegers().write(0, glowed.getEntityId()); //Set packet's entity id
	     com.comphenix.protocol.wrappers.WrappedDataWatcher watcher = new com.comphenix.protocol.wrappers.WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
	     com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer serializer = Registry.get(Byte.class); //Found this through google, needed for some stupid reason
	     watcher.setEntity(player); //Set the new data watcher's target
	     watcher.setObject(0, serializer, (byte) (0x00)); //Set status to glowing, found on protocol page
	     packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
	     try {
	    	 main.getProtocolManager().sendServerPacket(player, packet);
	         return true;
	     } catch (InvocationTargetException e) {
	         e.printStackTrace();
	         return false;
	     }
	}
	public void spawnAnimatronics() {
		ombreRef = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
	}
	public ArmorStand getBonnie() {
		return bonnie;
	}
	public ArmorStand getFreddy() {
		return freddy;
	}
	public ArmorStand getChica() {
		return chica;
	}
	public ArmorStand getFoxy() {
		return foxy;
	}
	public ArmorStand getMangle() {
		return mangle;
	}
	public ArmorStand getSpringtrap() {
		return springtrap;
	}
}
