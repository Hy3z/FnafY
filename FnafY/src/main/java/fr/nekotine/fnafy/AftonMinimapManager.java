package fr.nekotine.fnafy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;

import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.animation.ASAnimator;
import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.enums.Team;
import fr.nekotine.fnafy.events.ActionOnRoomEvent;
import fr.nekotine.fnafy.events.GameStartEvent;
import fr.nekotine.fnafy.events.GameStopEvent;
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
	private ASAnimator bonnieAnimator;
	private ArmorStand freddy;
	private ASAnimator freddyAnimator ;
	private ArmorStand chica;
	private ASAnimator chicaAnimator;
	private ArmorStand foxy;
	private ASAnimator foxyAnimator;
	private ArmorStand mangle;
	private ASAnimator mangleAnimator;
	private ArmorStand springtrap;
	private ASAnimator springtrapAnimator;
	public AftonMinimapManager(FnafYMain main) {
		this.main=main;
		bonnieAnimator = new ASAnimator(main, bonnie);
		freddyAnimator = new ASAnimator(main, freddy);
		chicaAnimator = new ASAnimator(main, chica);
		foxyAnimator = new ASAnimator(main, foxy);
		mangleAnimator = new ASAnimator(main, mangle);
		springtrapAnimator = new ASAnimator(main, springtrap);
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
			packetUnglow(e.getPlayer(), getAnimatronic(Animatronic.getFromWool(previousMat)));
			if(!newMat.equals(Material.AIR)) {
				packetGlow(e.getPlayer(), getAnimatronic(Animatronic.getFromWool(newMat)));
				Room r = getRoomFromWool(e.getPlayer());
				drawAftonOutline(r, e.getPlayer(), OUTLINE_GOLD);
				
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
				List<String> animations = main.getYamlReader().getRoomMinimapAnimation(main.getMapName(), e.getRoom().getRoomName(), Animatronic.getFromWool(getPlayerMaterialInHand(e.getPlayer())));
				//deplacer animatronic
			}else {
				//message de refus
			}
		}
	}
	private void moveAnimatronic(Player p, Room r, Animatronic anim) {
		List<String> animations = main.getYamlReader().getRoomMinimapAnimation(main.getMapName(), r.getRoomName(), Animatronic.getFromWool(getPlayerMaterialInHand(p)));
		ASAnimation animation = main.getAnimManager().getAsanims().get(animations.get((int)Math.random()*animations.size()));
	}
	private boolean packetGlow(Player player,Entity glowed) {
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
	}
	private boolean packetUnglow(Player player,Entity glowed) {
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
	private ASAnimation getRandomAnimatronicBaseAnimation(Animatronic anim) {
		String roomName = main.getYamlReader().getAnimatronicBaseRoomName(main.getMapName(), anim);
		List<String> animations = main.getYamlReader().getRoomMinimapAnimation(main.getMapName(), roomName, anim);
		return main.getAnimManager().getAsanims().get(animations.get((int)Math.random()*animations.size()));
	}
	private void spawnBonnie() {
		ASAnimation animation = getRandomAnimatronicBaseAnimation(Animatronic.BONNIE);
		Location animLoc = animation.getFrameOrder(0).pose.location;
		bonnie = (ArmorStand) animLoc.getWorld().spawnEntity(animLoc, EntityType.ARMOR_STAND);
		bonnieAnimator.setArmorStand(bonnie);
		bonnieAnimator.play(animation);
	}
	private void spawnFreddy() {
		ASAnimation animation = getRandomAnimatronicBaseAnimation(Animatronic.FREDDY);
		Location animLoc = animation.getFrameOrder(0).pose.location;
		freddy = (ArmorStand) animLoc.getWorld().spawnEntity(animLoc, EntityType.ARMOR_STAND);
		freddyAnimator.setArmorStand(freddy);
		freddyAnimator.play(animation);
	}
	private void spawnChica() {
		ASAnimation animation = getRandomAnimatronicBaseAnimation(Animatronic.CHICA);
		Location animLoc = animation.getFrameOrder(0).pose.location;
		chica = (ArmorStand) animLoc.getWorld().spawnEntity(animLoc, EntityType.ARMOR_STAND);
		chicaAnimator.setArmorStand(chica);
		chicaAnimator.play(animation);
	}
	private void spawnFoxy() {
		ASAnimation animation = getRandomAnimatronicBaseAnimation(Animatronic.FOXY);
		Location animLoc = animation.getFrameOrder(0).pose.location;
		foxy = (ArmorStand) animLoc.getWorld().spawnEntity(animLoc, EntityType.ARMOR_STAND);
		foxyAnimator.setArmorStand(foxy);
		foxyAnimator.play(animation);
	}
	private void spawnMangle() {
		ASAnimation animation = getRandomAnimatronicBaseAnimation(Animatronic.MANGLE);
		Location animLoc = animation.getFrameOrder(0).pose.location;
		mangle = (ArmorStand) animLoc.getWorld().spawnEntity(animLoc, EntityType.ARMOR_STAND);
		mangleAnimator.setArmorStand(mangle);
		mangleAnimator.play(animation);
	}
	private void spawnSpringtrap() {
		ASAnimation animation = getRandomAnimatronicBaseAnimation(Animatronic.SPRINGTRAP);
		Location animLoc = animation.getFrameOrder(0).pose.location;
		springtrap = (ArmorStand) animLoc.getWorld().spawnEntity(animLoc, EntityType.ARMOR_STAND);
		springtrapAnimator.setArmorStand(springtrap);
		springtrapAnimator.play(animation);
	}
	@EventHandler
	public void onGameStart(GameStartEvent e) {
		spawnBonnie();
		spawnFreddy();
		spawnChica();
		spawnFoxy();
		spawnMangle();
		spawnSpringtrap();
	}
	@EventHandler
	public void onGameEnd(GameStopEvent e) {
		bonnieAnimator.stop();
		freddyAnimator.stop();
		chicaAnimator.stop();
		foxyAnimator.stop();
		mangleAnimator.stop();
		springtrapAnimator.stop();
		
		bonnie.remove();
		freddy.remove();
		chica.remove();
		foxy.remove();
		mangle.remove();
		springtrap.remove();
	}
	private ArmorStand getAnimatronic(Animatronic anim) {
		switch(anim) {
		case BONNIE: return bonnie;
		case FREDDY:return freddy;
		case CHICA: return chica;
		case FOXY: return foxy;
		case MANGLE: return mangle;
		case SPRINGTRAP: return springtrap;
		default: return null;
		}
	}
	private ASAnimator getAnimatronicAnimator(Animatronic anim) {
		switch(anim) {
		case BONNIE: return bonnieAnimator;
		case FREDDY:return freddyAnimator;
		case CHICA: return chicaAnimator;
		case FOXY: return foxyAnimator;
		case MANGLE: return mangleAnimator;
		case SPRINGTRAP: return springtrapAnimator;
		default: return null;
		}
	}
}
