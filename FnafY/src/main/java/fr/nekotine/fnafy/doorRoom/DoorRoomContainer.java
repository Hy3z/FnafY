package fr.nekotine.fnafy.doorRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.animatronic.Animatronic;
import fr.nekotine.fnafy.events.GameStopEvent;
import fr.nekotine.fnafy.events.GameTickEvent;

public class DoorRoomContainer implements Listener{
	private final HashMap<String, Room> rooms = new HashMap<>();
	private final HashMap<String, Door> doors = new HashMap<>();
	public boolean fireDoorClosed=false;
	public Inventory visionInv;
	public boolean visionOnCooldown=false;
	public int visionCooldown=0;
	public final int VISIONTOTALCOOLDOWN=20*60;
	public DoorRoomContainer(FnafYMain main, HashMap<String, Room> rooms, HashMap<String, Door> doors) {
		this.rooms.putAll(rooms);
		this.doors.putAll(doors);
		
		visionInv = Bukkit.createInventory(null, 27, "Choisissez une caméra à débloquer");
		int slot=0;
		for(Room r : rooms.values()) {
			if(r.canGuardUnlockCamera && r.isUnlockedByGuard()) {
				visionInv.setItem(slot, createItemStack(r.roomMaterial, r.getRoomName(), ""));
				slot++;
			}
		}
		
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void onGameStop(GameStopEvent e) {
		HandlerList.unregisterAll(this);
	}
	@EventHandler
	public void gameTick(GameTickEvent e) {
		if(visionOnCooldown) {
			visionCooldown--;
			if(visionCooldown==0) {
				visionOnCooldown=false;
			}
		}
	}
	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		if(e.getInventory().equals(visionInv)) {
			if(e.getCurrentItem().getType()!=Material.AIR) {
				rooms.get(e.getCurrentItem().getItemMeta().getDisplayName()).setUnlockedByGuard(true);
				visionInv.setItem(e.getRawSlot(),  new ItemStack(Material.AIR));
				for(HumanEntity he : e.getViewers()) {
					he.closeInventory();
					he.sendMessage(ChatColor.GREEN+"Succefully unlocked camera "+ChatColor.WHITE+"["+ChatColor.GOLD+e.getCurrentItem().getItemMeta().getDisplayName()+ChatColor.WHITE+"]");
				}
				visionCooldown=VISIONTOTALCOOLDOWN;
				visionOnCooldown=true;
			}
		}
	}
	public void openVisionInventory(Player p) {
		p.openInventory(visionInv);
	}
	private ItemStack createItemStack(Material m, String name, String... lore) {
		ItemStack is = new ItemStack(m);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		List<String> lorelist = new ArrayList<String>();
		for(String s : lore) {
			lorelist.add(s);
		}
		im.setLore(lorelist);
		return is;
	}
	public Door getDoor(String doorName) {
		return doors.get(doorName);
	}
	public Room getRoom(String roomName) {
		return rooms.get(roomName);
	}
	public HashMap<String, Room> getRooms(){
		return rooms;
	}
	public List<Door> getCoupeFeuxDoors(){
		List<Door> l = new ArrayList<>();
		for(Door d : doors.values()) {
			if(d.getDoorType()==DoorType.COUPEFEUX) {
				l.add(d);
			}
		}
		return l;
	}
	public void switchFireDoors() {
		fireDoorClosed= !fireDoorClosed;
		for(Door d : doors.values()) {
			if(d.getDoorType()==DoorType.COUPEFEUX) {
				d.isClosed=fireDoorClosed;
			}
		}
	}
	public List<Room> canMoveFromList(Room r){
		List<Room> canMoveFromTo = new ArrayList<>();
		for(Door d : doors.values()) {
			Room newRoom = d.canMoveTo(r);
			if(newRoom!=null) {
				canMoveFromTo.add(newRoom);
			}
		}
		return canMoveFromTo;
	}
	public List<Room> cannotMoveFromList(Room r){
		List<Room> cannotMoveFromTo = new ArrayList<>();
		List<Room> canMoveFromTo = canMoveFromList(r);
		for(Room room : rooms.values()) {
			if(!canMoveFromTo.contains(room)) {
				cannotMoveFromTo.add(room);
			}
		}
		return cannotMoveFromTo;
	}
	public boolean canMoveFromToBool(Room prev, Room next) {
		for(Door d : doors.values()) {
			if(d.canMoveFromToBoolean(prev, next)) {
				return true;
			}
		}
		return false;
	}
	public List<Door> canMoveFromToDoorList(Room prev, Room next) {
		List<Door> doorList = new ArrayList<>();
		for(Door d : doors.values()) {
			if(d.canMoveFromToBoolean(prev, next)) {
				doorList.add(d);
			}
		}
		return doorList;
	}
	public List<ASAnimation> getDoorAnimationFromRoomToRoom(Room prev, Room next, Animatronic anim){
		List<Door> doorList = canMoveFromToDoorList(prev, next);
		if(doorList.size()>0) {
			return doorList.get((int)Math.random()*doorList.size()).getDoorAnimationsToRoom(next, anim);
		}
		return null;
	}
	public List<ASAnimation> getDoorMinimapAnimationFromRoomToRoom(Room prev, Room next, Animatronic anim){
		List<Door> doorList = canMoveFromToDoorList(prev, next);
		if(doorList.size()>0) {
			return doorList.get((int)Math.random()*doorList.size()).getMinimapDoorAnimationsToRoom(next, anim);
		}
		return null;
	}
}
